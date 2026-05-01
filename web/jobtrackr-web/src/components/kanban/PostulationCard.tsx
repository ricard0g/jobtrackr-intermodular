import type { Postulation } from "@/types/postulation";
import { useSortable } from "@dnd-kit/react/sortable";
import { useEffect, useRef } from "react";

interface PostulationCardProps {
	index: number;
	status: string;
	postulation: Postulation;
	onOpenDetails: (postulation: Postulation) => void;
}

export function PostulationCard({
	index,
	status,
	postulation,
	onOpenDetails,
}: PostulationCardProps) {
	const { ref, isDragging } = useSortable({
		id: postulation.postulacionId,
		index,
		group: status,
		type: "item",
		accept: "item",
		plugins: [],
	});
	const wasDraggingRef = useRef(false);

	useEffect(() => {
		if (isDragging) {
			wasDraggingRef.current = true;
		}
	}, [isDragging]);

	const formatLocalDate = (date: string): string => {
		const [year, month, day] = date.split("-").map(Number);

		return new Intl.DateTimeFormat("es-ES", {
			dateStyle: "medium",
		}).format(new Date(year, month - 1, day));
	};

	const openDetails = () => {
		if (wasDraggingRef.current) {
			wasDraggingRef.current = false;
			return;
		}

		onOpenDetails(postulation);
	};

	return (
		<div
			ref={ref}
			data-dragging={isDragging}
			role="button"
			tabIndex={0}
			onClick={openDetails}
			onKeyDown={(event) => {
				if (event.key === "Enter" || event.key === " ") {
					event.preventDefault();
					onOpenDetails(postulation);
				}
			}}
			className="flex flex-col justify-start items-start gap-y-3 bg-white p-4 rounded-lg border border-off-white shadow-md cursor-grab focus-visible:ring-3 focus-visible:ring-ring/30 focus-visible:outline-none"
		>
			<div className="flex items-center justify-start gap-x-3">
				<img
					className="w-10 h-10 rounded-md"
					src={postulation.empresa.logoEmpresa}
					alt={`${postulation.empresa.nombreEmpresa} logo`}
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
			<div className="flex w-full gap-x-1 overflow-x-scroll scrollbar-hide">
				{postulation.tagList.slice(0, 4).map((tag) => (
					<span
						key={tag.etiquetaId}
						title={tag.nombreEtiqueta}
						className="inline-flex h-6 w-20 items-center justify-center truncate rounded-full px-2 text-xs"
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
					{postulation.salarioMinimo && postulation.salarioMaximo
						? `€${Math.round(postulation.salarioMinimo / 1000)}k - €${Math.round(postulation.salarioMaximo / 1000)}k`
						: "Salario no indicado"}
				</p>
				<p className="inline-block text-sm text-medium-gray">
					{formatLocalDate(postulation.fechaPostulacion)}
				</p>
			</div>
		</div>
	);
}
