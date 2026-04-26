import {
	DragDropProvider,
	type DragEndEvent,
	type DragOverEvent,
	type DragStartEvent,
} from "@dnd-kit/react";
import { move } from "@dnd-kit/helpers";
import { postulationStatus } from "@/types/postulationStatus";
import { StatusColumn } from "./StatusColumn";
import type { Postulation } from "@/types/postulation";
import { useLoaderData } from "react-router";
import { use, useState, useMemo } from "react";
import { PostulationCard } from "./PostulationCard";

interface KanbanBoardLoaderData {
	postulationsPromise: Promise<Postulation[]>;
}

export function KanbanBoard() {
	const { postulationsPromise } = useLoaderData<KanbanBoardLoaderData>();
	const postulations = use(postulationsPromise);
	const [postulationsState, setPostulationsState] = useState<
		Record<string, Postulation[]>
	>(() => {
		if (!postulations) return {};

		return Object.fromEntries(
			postulationStatus.map((status) => [
				status[0],
				postulations.filter(
					(p) => p.estatus === status[0].toUpperCase(),
				),
			]),
		);
	});

	console.log(postulationsState);
	console.log(postulations);

	return (
		<DragDropProvider
			// onDragOver={(event) => {
			// 	setPostulationsState((items) => move(items, event));
			// }}
		>
			<div className="flex gap-x-4 max-w-full w-fit h-full py-8 px-8 overflow-x-scroll">
				{postulationStatus.map((status) => (
					<StatusColumn status={status[0]} columnColor={status[1]} postulations={postulationsState[status[0]]} />
				))}
			</div>
		</DragDropProvider>
	);
}
