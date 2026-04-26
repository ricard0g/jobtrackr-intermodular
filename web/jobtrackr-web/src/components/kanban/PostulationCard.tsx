import type { Postulation } from "@/types/postulation";
import { useSortable } from "@dnd-kit/react/sortable";

interface PostulationCardProps {
	postulationId: number;
	index: number;
	status: string;
	postulation: Postulation;
}

export function PostulationCard({
	// postulationId,
	index,
	status,
	postulation
}: PostulationCardProps) {
	const { ref, isDragging } = useSortable({
		id: postulation.postulacionId,
		index,
		group: status,
		type: "item",
		accept: "item",
	});

	const formatLocalDate = (date: string): string => {
		const [year, month, day] = date.split("-").map(Number);

		return new Intl.DateTimeFormat("es-ES", {
			dateStyle: "medium",
		}).format(new Date(year, month - 1, day));
	};

	return (
		<div
			ref={ref}
			data-dragging={isDragging}
			className="flex flex-col justify-start items-start gap-y-3 bg-white p-4 rounded-lg border border-off-white shadow-md cursor-grab"
		>
			<div className="flex items-center justify-start gap-x-3">
				<img
					className="w-10 h-10 rounded-md"
					src={postulation.empresa.logoEmpresa}
				/>
				<div>
					<p className="font-bold font-display">
						{postulation.empresa.nombreEmpresa}
					</p>
					<p className="text-sm text-medium-gray">
						{postulation.rol}
					</p>
				</div>
			</div>
			<div className="flex gap-x-1">
				{postulation.tagList.slice(0, 4).map((tag) => (
					<span
						className="text-xs py-0.5 px-3 rounded-full"
						style={{
							color: tag.colorEtiqueta,
							border: `1px solid ${tag.colorEtiqueta}`,
							backgroundColor: `${tag.colorEtiqueta}22`,
						}}
					>
						{tag.nombreEtiqueta}
					</span>
				))}
			</div>
			<div className="w-full h-px bg-light-gray"></div>
			<div className="flex justify-between items-center w-full">
				<p className="inline-block font-semibold text-sm">
					€{postulation.salarioMinimo.toString().slice(0, 2)}k - €
					{postulation.salarioMaximo.toString().slice(0, 2)}k
				</p>
				<p className="inline-block text-sm text-medium-gray">
					{formatLocalDate(postulation.fechaPostulacion)}
				</p>
			</div>
		</div>
	);
}
