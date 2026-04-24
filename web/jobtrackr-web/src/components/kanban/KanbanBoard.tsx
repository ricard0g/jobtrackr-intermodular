import { DragDropProvider } from "@dnd-kit/react";
import { PostulationStatus } from "@/types/postulationStatus";
import { StatusColumn } from "./StatusColumn";
import type { Postulation } from "@/types/postulation";
import { useLoaderData } from "react-router";
import { use } from "react";

interface KanbanBoardLoaderData {
	postulationsPromise: Promise<Postulation[]>;
}

export function KanbanBoard() {
	const { postulationsPromise } = useLoaderData<KanbanBoardLoaderData>();
	const postulations = use(postulationsPromise);

	const postulationStatus = [
		PostulationStatus.POSTULADA,
		PostulationStatus.REVISION,
		PostulationStatus.ENTREVISTA,
		PostulationStatus.OFERTA,
		PostulationStatus.DESCARTADA,
		PostulationStatus.RETIRADA,
	];

	return (
		<DragDropProvider>
			<div className="flex gap-x-4 max-w-full w-fit h-full py-8 px-8 overflow-x-scroll">
				{postulationStatus.map((status) => {
					const statusPostulations = postulations.filter(
						(postulation) =>
							postulation.estatus === status[0].toUpperCase(),
					);

					return (
						<StatusColumn
							status={status[0]}
							columnColor={status[1]}
							postulations={statusPostulations}
						/>
					);
				})}
			</div>
		</DragDropProvider>
	);
}
