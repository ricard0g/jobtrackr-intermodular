import { useDroppable } from "@dnd-kit/react";
import { Button } from "../ui/button";
import { Plus } from "lucide-react";
import type { Postulation } from "@/types/postulation";

interface StatusColumnProps {
	status: string;
	columnColor: string;
    postulations: Postulation[];
}

export function StatusColumn({ status, columnColor, postulations }: StatusColumnProps) {
	const { ref } = useDroppable({
		id: status,
	});

	return (
		<div
			ref={ref}
			className="h-[85vh] min-w-[20vw] max-w-[20vw] bg-off-white rounded-lg border border-light-gray shadow-cool-light p-4"
		>
			<div className="flex justify-between items-center">
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
				<Button variant="secondary" className="hover:bg-light-gray rounded-lg">
					<Plus />
				</Button>
			</div>
            
            <div>
                {postulations.map(postulation => (
                    <div>{postulation.rol}</div>
                ))}
            </div>
		</div>
	);
}
