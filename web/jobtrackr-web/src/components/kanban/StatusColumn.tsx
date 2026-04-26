import { useDroppable } from "@dnd-kit/react";
import { Button } from "../ui/button";
import { Plus } from "lucide-react";
import { PostulationCard } from "./PostulationCard";
import { CollisionPriority } from "@dnd-kit/abstract";
import type { Postulation } from "@/types/postulation";
import { Children, type ReactNode } from "react";

interface StatusColumnProps {
	status: string;
	columnColor: string;
	// postulationIds: number[];
	postulations: Postulation[];
	// children: ReactNode[];
}

export function StatusColumn({
	status,
	columnColor,
	// children,
	// postulationIds,
	postulations,
}: StatusColumnProps) {
	const { ref } = useDroppable({
		id: status,
		type: "column",
		accept: "item",
		collisionPriority: CollisionPriority.Low,
	});

	return (
		<div
			ref={ref}
			className="h-[85vh] min-w-[20vw] max-w-[20vw] bg-off-white rounded-lg border border-light-gray shadow-cool-light p-4"
		>
			<div className="flex justify-between items-center mb-2">
				<div className="flex items-center justify-start gap-x-2">
					<div
						className="w-2 h-2"
						style={{
							backgroundColor: columnColor,
							borderRadius: "50%",
						}}
					></div>
					{status}
				</div>
				<Button
					variant="secondary"
					className="hover:bg-light-gray rounded-lg"
				>
					<Plus />
				</Button>
			</div>

			<div className="flex flex-col gap-y-2">
				{postulations.map((p, index) => (
					<PostulationCard
						key={p.postulacionId}
						postulationId={p.postulacionId}
						index={index}
						status={status}
						postulation={p}
					/>
				))}
			</div>
		</div>
	);
}
