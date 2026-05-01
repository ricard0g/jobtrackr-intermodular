import { useDroppable } from "@dnd-kit/react";
import { Button } from "../ui/button";
import { Plus } from "lucide-react";
import { PostulationCard } from "./PostulationCard";
import { CollisionPriority } from "@dnd-kit/abstract";
import type { Postulation } from "@/types/postulation";

interface StatusColumnProps {
	status: string;
	columnColor: string;
	postulations: Postulation[];
	onOpenDetails: (postulation: Postulation) => void;
}

export function StatusColumn({
	status,
	columnColor,
	postulations,
	onOpenDetails,
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
			className="h-[85vh] min-w-[20vw] max-w-[20vw] overflow-y-hidden  bg-off-white rounded-lg border border-light-gray shadow-cool-light p-4"
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

			<div className="flex flex-col gap-y-2 max-h-full pb-10 scrollbar-hide overflow-y-scroll">
				{postulations.map((p, index) => (
					<PostulationCard
						key={p.postulacionId}
						index={index}
						status={status}
						postulation={p}
						onOpenDetails={onOpenDetails}
					/>
				))}
			</div>
		</div>
	);
}
