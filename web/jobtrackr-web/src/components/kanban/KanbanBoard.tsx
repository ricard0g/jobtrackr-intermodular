import { DragDropProvider, type DragEndEvent } from "@dnd-kit/react";
import { isSortable } from "@dnd-kit/react/sortable";
import { postulationStatus } from "@/types/postulationStatus";
import { StatusColumn } from "./StatusColumn";
import type { Postulation } from "@/types/postulation";
import { useLoaderData } from "react-router";
import { use, useState } from "react";

interface KanbanBoardLoaderData {
	postulationsPromise: Promise<Postulation[]>;
}

type StatusLabel = (typeof postulationStatus)[number][0];
type PostulationsByStatus = Record<StatusLabel, Postulation[]>;

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

export function KanbanBoard() {
	const { postulationsPromise } = useLoaderData<KanbanBoardLoaderData>();
	const postulations = use(postulationsPromise);
	const [postulationsState, setPostulationsState] =
		useState<PostulationsByStatus>(() =>
			groupPostulationsByStatus(postulations),
		);

	const handleDragEnd = (event: DragEndEvent) => {
		if (event.canceled) return;

		const { source, target } = event.operation;

		if (!isSortable(source) || source.type !== "item" || !target) return;

		const fromStatus = source.initialGroup;
		const fromIndex = source.initialIndex;

		if (!isStatusLabel(fromStatus)) return;

		let toStatus: StatusLabel | undefined;
		let toIndex: number | undefined;

		if (target.type === "column") {
			if (!isStatusLabel(target.id)) return;

			toStatus = target.id;
			toIndex = postulationsState[toStatus].length;
		} else if (isSortable(target)) {
			if (!isStatusLabel(target.group)) return;

			console.log(event);
			console.log(event.operation.shape?.current.center);
			console.log(event.operation.position.current);

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

		setPostulationsState((currentPostulations) =>
			movePostulation(
				currentPostulations,
				fromStatus,
				fromIndex,
				toStatus,
				toIndex,
			),
		);
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
					/>
				))}
			</div>
		</DragDropProvider>
	);
}
