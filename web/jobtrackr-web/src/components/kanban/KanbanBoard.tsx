import { DragDropProvider, type DragEndEvent } from "@dnd-kit/react";
import { isSortable } from "@dnd-kit/react/sortable";
import { postulationStatus } from "@/types/postulationStatus";
import { StatusColumn } from "./StatusColumn";
import type { Postulation } from "@/types/postulation";
import { useLoaderData } from "react-router";
import { use, useRef, useState } from "react";
import { PostulationDetailDrawer } from "../postulations/PostulationDetailDrawer";

interface KanbanBoardLoaderData {
	postulationsPromise: Promise<Postulation[]>;
}

type StatusLabel = (typeof postulationStatus)[number][0];
type PostulationsByStatus = Record<StatusLabel, Postulation[]>;
type OrderPatch = {
	postulacionId: number;
	ordenKanban: number;
};

const API_URL = import.meta.env.VITE_API_URL;
const statusLabels = postulationStatus.map(([status]) => status);

const isStatusLabel = (value: unknown): value is StatusLabel =>
	typeof value === "string" && statusLabels.includes(value as StatusLabel);

const toBackendStatus = (status: StatusLabel) => status.toUpperCase();

const normalizeKanbanOrder = (
	postulationsByStatus: PostulationsByStatus,
	statuses: StatusLabel[],
): PostulationsByStatus => {
	return {
		...postulationsByStatus,
		...Object.fromEntries(
			statuses.map((status) => [
				status,
				postulationsByStatus[status].map((postulation, index) => ({
					...postulation,
					estatus: toBackendStatus(status),
					ordenKanban: index,
				})),
			]),
		),
	};
};

const patchJson = async (path: string, body: object) => {
	const response = await fetch(`${API_URL}${path}`, {
		method: "PATCH",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(body),
	});

	if (!response.ok) {
		throw response;
	}
};

const patchPostulationStatus = async (
	postulacionId: number,
	estatus: string,
) => {
	await patchJson(`/postulaciones/${postulacionId}/estatus`, { estatus });
};

const patchPostulationOrder = async (
	postulacionId: number,
	ordenKanban: number,
) => {
	await patchJson(`/postulaciones/${postulacionId}/orden`, {
		ordenKanban,
	});
};

const groupPostulationsByStatus = (
	postulations: Postulation[],
): PostulationsByStatus => {
	return Object.fromEntries(
		postulationStatus.map(([status]) => [
			status,
			postulations
				.filter(
					(postulation) =>
						postulation.estatus === toBackendStatus(status),
				)
				.toSorted((a, b) => a.ordenKanban - b.ordenKanban),
		]),
	) as PostulationsByStatus;
};

const movePostulation = (
	postulationsByStatus: PostulationsByStatus,
	fromStatus: StatusLabel,
	fromIndex: number,
	toStatus: StatusLabel,
	toIndex: number,
): PostulationsByStatus => {
	const sourcePostulations = [...postulationsByStatus[fromStatus]];
	const [movedPostulation] = sourcePostulations.splice(fromIndex, 1);

	if (!movedPostulation) {
		return postulationsByStatus;
	}

	const targetPostulations =
		fromStatus === toStatus
			? sourcePostulations
			: [...postulationsByStatus[toStatus]];
	const adjustedTargetIndex =
		fromStatus === toStatus && fromIndex < toIndex ? toIndex - 1 : toIndex;
	const boundedTargetIndex = Math.max(
		0,
		Math.min(adjustedTargetIndex, targetPostulations.length),
	);

	targetPostulations.splice(boundedTargetIndex, 0, movedPostulation);

	return normalizeKanbanOrder(
		{
			...postulationsByStatus,
			[fromStatus]: sourcePostulations,
			[toStatus]: targetPostulations,
		},
		fromStatus === toStatus ? [fromStatus] : [fromStatus, toStatus],
	);
};

const getChangedOrderPatches = (
	previousPostulations: Postulation[],
	nextPostulations: Postulation[],
): OrderPatch[] => {
	const previousOrderById = new Map(
		previousPostulations.map((postulation) => [
			postulation.postulacionId,
			postulation.ordenKanban,
		]),
	);

	return nextPostulations
		.filter(
			(postulation) =>
				previousOrderById.get(postulation.postulacionId) !==
				postulation.ordenKanban,
		)
		.map(({ postulacionId, ordenKanban }) => ({
			postulacionId,
			ordenKanban,
		}));
};

const persistKanbanMove = async (
	previousPostulations: PostulationsByStatus,
	nextPostulations: PostulationsByStatus,
	movedPostulationId: number,
	fromStatus: StatusLabel,
	toStatus: StatusLabel,
) => {
	const orderPatches =
		fromStatus === toStatus
			? getChangedOrderPatches(
					previousPostulations[fromStatus],
					nextPostulations[fromStatus],
				)
			: [
					...getChangedOrderPatches(
						previousPostulations[fromStatus],
						nextPostulations[fromStatus],
					),
					...getChangedOrderPatches(
						previousPostulations[toStatus],
						nextPostulations[toStatus],
					),
				];

	if (fromStatus !== toStatus) {
		await patchPostulationStatus(
			movedPostulationId,
			toBackendStatus(toStatus),
		);
	}

	await Promise.all(
		orderPatches.map(({ postulacionId, ordenKanban }) =>
			patchPostulationOrder(postulacionId, ordenKanban),
		),
	);
};

export function KanbanBoard() {
	const { postulationsPromise } = useLoaderData<KanbanBoardLoaderData>();
	const postulations = use(postulationsPromise);
	const boardKey = postulations
		.map(
			(postulation) =>
				`${postulation.postulacionId}:${postulation.estatus}:${postulation.ordenKanban}:${postulation.actualizadaEn}`,
		)
		.join("|");

	return <KanbanBoardContent key={boardKey} postulations={postulations} />;
}

function KanbanBoardContent({
	postulations,
}: {
	postulations: Postulation[];
}) {
	const [postulationsState, setPostulationsState] =
		useState<PostulationsByStatus>(() =>
			groupPostulationsByStatus(postulations),
		);
	const [selectedPostulation, setSelectedPostulation] =
		useState<Postulation | null>(null);
	const persistenceVersionRef = useRef(0);

	const handleDragEnd = (event: DragEndEvent) => {
		if (event.canceled) return;

		const { source, target } = event.operation;

		if (!isSortable(source) || source.type !== "item" || !target) return;

		const fromStatus = source.initialGroup;
		const fromIndex = source.initialIndex;
		const movedPostulationId =
			typeof source.id === "number" ? source.id : Number(source.id);

		if (!isStatusLabel(fromStatus) || Number.isNaN(movedPostulationId))
			return;

		let toStatus: StatusLabel | undefined;
		let toIndex: number | undefined;

		if (target.type === "column") {
			if (!isStatusLabel(target.id)) return;

			toStatus = target.id;
			toIndex = postulationsState[toStatus].length;
		} else if (isSortable(target)) {
			if (!isStatusLabel(target.group)) return;

			const activeCenter =
				event.operation.shape?.current.center ??
				event.operation.position.current;
			const targetShape = target.shape;
			const isBelowTarget = targetShape
				? Math.round(activeCenter.y) > Math.round(targetShape.center.y)
				: false;

			toStatus = target.group;
			toIndex = target.index + (isBelowTarget ? 1 : 0);
		}

		if (!toStatus || toIndex === undefined) return;
		if (fromStatus === toStatus && fromIndex === toIndex) return;

		const previousPostulations = postulationsState;
		const nextPostulations = movePostulation(
			previousPostulations,
			fromStatus,
			fromIndex,
			toStatus,
			toIndex,
		);
		const persistenceVersion = persistenceVersionRef.current + 1;
		persistenceVersionRef.current = persistenceVersion;

		setPostulationsState(nextPostulations);

		void persistKanbanMove(
			previousPostulations,
			nextPostulations,
			movedPostulationId,
			fromStatus,
			toStatus,
		).catch(() => {
			if (persistenceVersionRef.current === persistenceVersion) {
				setPostulationsState(previousPostulations);
			}
		});
	};

	return (
		<DragDropProvider onDragEnd={handleDragEnd}>
			<div className="flex gap-x-4 max-w-full w-fit h-full py-8 px-8 overflow-x-scroll">
				{postulationStatus.map(([status, columnColor]) => (
					<StatusColumn
						key={status}
						status={status}
						columnColor={columnColor}
						postulations={postulationsState[status]}
						onOpenDetails={setSelectedPostulation}
					/>
				))}
			</div>
			<PostulationDetailDrawer
				open={selectedPostulation !== null}
				onOpenChange={(open) => {
					if (!open) setSelectedPostulation(null);
				}}
				postulation={selectedPostulation}
			/>
		</DragDropProvider>
	);
}
