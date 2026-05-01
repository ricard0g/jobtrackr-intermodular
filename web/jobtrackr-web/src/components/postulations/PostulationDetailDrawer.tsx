import { Loader2, Plus, Trash2, X } from "lucide-react";
import { type FormEvent, useEffect, useMemo, useState } from "react";

import { Button } from "@/components/ui/button";
import {
	Dialog,
	DialogClose,
	DialogContent,
	DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import {
	Select,
	SelectContent,
	SelectItem,
	SelectTrigger,
	SelectValue,
} from "@/components/ui/select";
import type {
	CreateInterviewRequest,
	Interview,
	InterviewResult,
	InterviewType,
} from "@/types/interview";
import type { Postulation } from "@/types/postulation";
import { postulationStatus } from "@/types/postulationStatus";

const API_URL = import.meta.env.VITE_API_URL;

interface PostulationDetailDrawerProps {
	open: boolean;
	onOpenChange: (open: boolean) => void;
	postulation: Postulation | null;
}

type InterviewFormValues = {
	tipoEntrevista: InterviewType;
	fechaEntrevista: string;
	entrevistador: string;
	resultadoEntrevista: InterviewResult;
};

const interviewTypeOptions: Array<{
	value: InterviewType;
	label: string;
}> = [
	{ value: "PERSONAL", label: "Entrevista Personal" },
	{ value: "TELEFONICA", label: "Entrevista Telefonica" },
	{ value: "TECNICA", label: "Entrevista Tecnica" },
	{ value: "ARQUITECTURA", label: "Entrevista Arquitectura" },
	{ value: "RRHH", label: "Entrevista RRHH" },
	{ value: "FINAL", label: "Entrevista Final" },
];

const resultBadgeStyles: Record<
	InterviewResult,
	{
		label: string;
		className: string;
	}
> = {
	PENDIENTE: {
		label: "⏳ Pendiente",
		className: "border-yellow-300 bg-yellow-100 text-yellow-800",
	},
	SUPERADA: {
		label: "✅ Superada",
		className: "border-green-300 bg-green-100 text-green-800",
	},
	FALLIDA: {
		label: "❌ Fallida",
		className: "border-red-300 bg-red-100 text-red-800",
	},
};

const initialInterviewValues = (): InterviewFormValues => ({
	tipoEntrevista: "PERSONAL",
	fechaEntrevista: "",
	entrevistador: "",
	resultadoEntrevista: "PENDIENTE",
});

const formatLocalDate = (date: string | null | undefined): string => {
	if (!date) return "No indicada";

	const [year, month, day] = date.split("-").map(Number);

	if (!year || !month || !day) return "No indicada";

	return new Intl.DateTimeFormat("en-US", {
		month: "short",
		day: "numeric",
		year: "numeric",
	}).format(new Date(year, month - 1, day));
};

const formatLocalDateTime = (dateTime: string): string => {
	const [datePart] = dateTime.split(" ");

	return formatLocalDate(datePart);
};

const formatSalaryValue = (value: number | null | undefined): string | null => {
	if (value === null || value === undefined || !Number.isFinite(value)) {
		return null;
	}

	return `${Math.round(value / 1000)}k`;
};

const formatSalaryRange = (
	min: number | null | undefined,
	max: number | null | undefined,
): string => {
	const minValue = formatSalaryValue(min);
	const maxValue = formatSalaryValue(max);

	if (minValue && maxValue) return `€${minValue} - ${maxValue}`;
	if (minValue) return `Desde €${minValue}`;
	if (maxValue) return `Hasta €${maxValue}`;

	return "No indicado";
};

const getStatusDisplay = (statusValue: string) => {
	const status = postulationStatus.find(
		([label]) => label.toUpperCase() === statusValue,
	);

	return {
		label: status?.[0] ?? statusValue,
		color: status?.[1] ?? "#666",
	};
};

const getInterviewTypeLabel = (type: InterviewType): string =>
	interviewTypeOptions.find((option) => option.value === type)?.label ?? type;

const toBackendDateTime = (dateTimeLocal: string): string =>
	`${dateTimeLocal.replace("T", " ")}:00`;

const readApiError = async (response: Response, fallback: string) => {
	try {
		const body = (await response.json()) as { error?: string };

		return body.error ?? fallback;
	} catch {
		return fallback;
	}
};

function DetailBox({
	label,
	children,
}: {
	label: string;
	children: React.ReactNode;
}) {
	return (
		<div className="min-w-0 rounded-md border border-light-gray bg-off-white p-1 text-left">
			<p className="text-xs font-normal text-medium-gray">{label}</p>
			<div className="mt-1 min-w-0 text-base font-medium text-black">
				{children}
			</div>
		</div>
	);
}

export function PostulationDetailDrawer({
	open,
	onOpenChange,
	postulation,
}: PostulationDetailDrawerProps) {
	const [interviews, setInterviews] = useState<Interview[]>([]);
	const [isLoadingInterviews, setIsLoadingInterviews] = useState(false);
	const [interviewError, setInterviewError] = useState<string | null>(null);
	const [isAddFormOpen, setIsAddFormOpen] = useState(false);
	const [formValues, setFormValues] = useState<InterviewFormValues>(() =>
		initialInterviewValues(),
	);
	const [formError, setFormError] = useState<string | null>(null);
	const [isSubmittingInterview, setIsSubmittingInterview] = useState(false);
	const [deletingInterviewId, setDeletingInterviewId] = useState<
		number | null
	>(null);

	const statusDisplay = useMemo(
		() => getStatusDisplay(postulation?.estatus ?? ""),
		[postulation?.estatus],
	);

	const nextRound = useMemo(
		() =>
			interviews.reduce(
				(maxRound, interview) =>
					Math.max(maxRound, interview.numeroRonda),
				0,
			) + 1,
		[interviews],
	);

	const resetDrawerState = () => {
		setInterviews([]);
		setInterviewError(null);
		setIsAddFormOpen(false);
		setFormValues(initialInterviewValues());
		setFormError(null);
		setDeletingInterviewId(null);
	};

	useEffect(() => {
		if (!open || !postulation) return;

		const controller = new AbortController();

		const fetchInterviews = async () => {
			resetDrawerState();
			setIsLoadingInterviews(true);
			setInterviewError(null);

			try {
				const response = await fetch(
					`${API_URL}/postulaciones/${postulation.postulacionId}/entrevistas`,
					{ signal: controller.signal },
				);

				if (!response.ok) {
					throw new Error(
						await readApiError(
							response,
							"No se pudieron cargar las entrevistas.",
						),
					);
				}

				const data = (await response.json()) as Interview[];

				setInterviews(
					data.toSorted(
						(a, b) => a.numeroRonda - b.numeroRonda,
					),
				);
			} catch (error) {
				if (error instanceof DOMException && error.name === "AbortError") {
					return;
				}

				setInterviewError(
					error instanceof Error
						? error.message
						: "No se pudieron cargar las entrevistas.",
				);
			} finally {
				setIsLoadingInterviews(false);
			}
		};

		void fetchInterviews();

		return () => controller.abort();
	}, [open, postulation]);

	if (!postulation) return null;

	const updateFormValue = <T extends keyof InterviewFormValues>(
		name: T,
		value: InterviewFormValues[T],
	) => {
		setFormValues((currentValues) => ({
			...currentValues,
			[name]: value,
		}));
		setFormError(null);
	};

	const handleCreateInterview = async (
		event: FormEvent<HTMLFormElement>,
	) => {
		event.preventDefault();

		if (!formValues.fechaEntrevista) {
			setFormError("Indica la fecha de la entrevista.");
			return;
		}

		if (!formValues.entrevistador.trim()) {
			setFormError("Indica el nombre del entrevistador.");
			return;
		}

		const payload: CreateInterviewRequest = {
			postulacionId: postulation.postulacionId,
			numeroRonda: nextRound,
			tipoEntrevista: formValues.tipoEntrevista,
			fechaEntrevista: toBackendDateTime(formValues.fechaEntrevista),
			entrevistador: formValues.entrevistador.trim(),
			resultadoEntrevista: formValues.resultadoEntrevista,
		};

		setIsSubmittingInterview(true);
		setFormError(null);

		try {
			const response = await fetch(
				`${API_URL}/postulaciones/${postulation.postulacionId}/entrevistas`,
				{
					method: "POST",
					body: JSON.stringify(payload),
				},
			);

			if (!response.ok) {
				throw new Error(
					await readApiError(
						response,
						"No se pudo crear la entrevista.",
					),
				);
			}

			const interviewsResponse = await fetch(
				`${API_URL}/postulaciones/${postulation.postulacionId}/entrevistas`,
			);

			if (interviewsResponse.ok) {
				const data = (await interviewsResponse.json()) as Interview[];

				setInterviews(
					data.toSorted(
						(a, b) => a.numeroRonda - b.numeroRonda,
					),
				);
			}

			setFormValues(initialInterviewValues());
			setIsAddFormOpen(false);
		} catch (error) {
			setFormError(
				error instanceof Error
					? error.message
					: "No se pudo crear la entrevista.",
			);
		} finally {
			setIsSubmittingInterview(false);
		}
	};

	const handleDeleteInterview = async (interview: Interview) => {
		const confirmed = window.confirm(
			"¿Eliminar esta entrevista? Esta accion no se puede deshacer.",
		);

		if (!confirmed) return;

		setDeletingInterviewId(interview.entrevistaId);
		setInterviewError(null);

		try {
			const response = await fetch(
				`${API_URL}/postulaciones/${postulation.postulacionId}/entrevistas/${interview.entrevistaId}`,
				{ method: "DELETE" },
			);

			if (!response.ok) {
				throw new Error(
					await readApiError(
						response,
						"No se pudo eliminar la entrevista.",
					),
				);
			}

			setInterviews((currentInterviews) =>
				currentInterviews.filter(
					(currentInterview) =>
						currentInterview.entrevistaId !==
						interview.entrevistaId,
				),
			);
		} catch (error) {
			setInterviewError(
				error instanceof Error
					? error.message
					: "No se pudo eliminar la entrevista.",
			);
		} finally {
			setDeletingInterviewId(null);
		}
	};

	return (
		<Dialog
			open={open}
			onOpenChange={(nextOpen) => {
				if (!nextOpen) resetDrawerState();
				onOpenChange(nextOpen);
			}}
		>
			<DialogContent
				showCloseButton={false}
				className="top-[2.5dvh] right-[2.5dvh] left-auto h-[95dvh] max-h-[95dvh] w-[min(calc(100vw-5dvh),560px)] max-w-none auto-rows-max content-start items-start translate-x-0 translate-y-0 gap-5 overflow-y-auto rounded-[var(--rounded-default)] border border-light-gray bg-white p-4 shadow-cool-strong duration-300 data-[state=closed]:slide-out-to-right data-[state=open]:slide-in-from-right data-[state=closed]:zoom-out-100 data-[state=open]:zoom-in-100"
			>
				<DialogTitle className="sr-only">
					Detalle de postulacion
				</DialogTitle>
				<DialogClose asChild>
					<Button
						type="button"
						variant="ghost"
						size="icon"
						className="absolute top-4 right-4"
						aria-label="Cerrar detalle"
					>
						<X />
					</Button>
				</DialogClose>

				<header className="flex items-start gap-4 pr-12">
					<img
						className="h-20 w-20 shrink-0 overflow-visible rounded-md object-cover p-0.5"
						src={postulation.empresa.logoEmpresa}
						alt={`${postulation.empresa.nombreEmpresa} logo`}
					/>
					<div className="min-w-0 pt-1">
						<h2 className="truncate font-display text-2xl font-bold text-black">
							{postulation.empresa.nombreEmpresa}
						</h2>
						<div className="mt-1 flex flex-wrap items-center gap-2">
							<p className="text-base font-normal text-medium-gray">
								{postulation.rol}
							</p>
							<span
								className="rounded-full border px-2.5 py-0.5 text-xs font-medium"
								style={{
									color: statusDisplay.color,
									borderColor: statusDisplay.color,
									backgroundColor: `${statusDisplay.color}22`,
								}}
							>
								{statusDisplay.label}
							</span>
						</div>
					</div>
				</header>

				<section className="grid max-h-[45dvh] gap-2 overflow-y-auto pr-1">
					<h3 className="text-left text-base font-normal text-medium-gray">
						Detalles
					</h3>
					<div className="grid grid-cols-2 gap-2">
						<DetailBox label="Salario">
							{formatSalaryRange(
								postulation.salarioMinimo,
								postulation.salarioMaximo,
							)}
						</DetailBox>
						<DetailBox label="Ubicacion">
							{postulation.esTelematico
								? "Remoto"
								: postulation.ubicacion || "No indicada"}
						</DetailBox>
						<DetailBox label="Fecha postulacion">
							{formatLocalDate(postulation.fechaPostulacion)}
						</DetailBox>
						<DetailBox label="Oferta">
							{postulation.ofertaUrl ? (
								<a
									href={postulation.ofertaUrl}
									target="_blank"
									rel="noreferrer"
									className="block truncate text-dark-accent underline-offset-2 hover:underline"
								>
									{postulation.ofertaUrl}
								</a>
							) : (
								"No indicada"
							)}
						</DetailBox>
					</div>
				</section>

				{postulation.tagList.length > 0 && (
					<section className="flex w-full flex-wrap gap-2">
						{postulation.tagList.map((tag) => (
							<span
								key={tag.etiquetaId}
								title={tag.nombreEtiqueta}
								className="inline-flex h-6 w-24 items-center justify-center truncate rounded-full border px-2 text-xs"
								style={{
									color: tag.colorEtiqueta,
									borderColor: tag.colorEtiqueta,
									backgroundColor: `${tag.colorEtiqueta}22`,
								}}
							>
								{tag.nombreEtiqueta}
							</span>
						))}
					</section>
				)}

				<section className="grid gap-2">
					<h3 className="text-left text-base font-normal text-medium-gray">
						Ronda Entrevista
					</h3>

					{isLoadingInterviews && (
						<div className="flex items-center gap-2 rounded-md border border-light-gray bg-off-white p-3 text-sm text-medium-gray">
							<Loader2 className="size-4 animate-spin" />
							Cargando entrevistas
						</div>
					)}

					{interviewError && (
						<p className="rounded-md border border-red-200 bg-red-50 p-3 text-sm text-red-700">
							{interviewError}
						</p>
					)}

					{!isLoadingInterviews &&
						!interviewError &&
						interviews.length === 0 && (
							<p className="rounded-md border border-light-gray bg-off-white p-3 text-sm text-medium-gray">
								Todavia no hay entrevistas para esta postulacion.
							</p>
						)}

					<div className="grid max-h-[28dvh] gap-2 overflow-y-auto pr-1">
						{interviews.map((interview) => {
							const resultBadge =
								resultBadgeStyles[
									interview.resultadoEntrevista
								];

							return (
								<article
									key={interview.entrevistaId}
									className="relative rounded-md border border-light-gray bg-off-white p-2 pr-11 text-left"
								>
									<h4 className="text-sm font-bold text-black">
										{getInterviewTypeLabel(
											interview.tipoEntrevista,
										)}
									</h4>
									<div className="mt-1 flex flex-wrap gap-x-3 gap-y-1 text-sm text-medium-gray">
										<span>
											📅{" "}
											{formatLocalDateTime(
												interview.fechaEntrevista,
											)}
										</span>
										<span>
											👤{" "}
											{interview.entrevistador ||
												"Sin entrevistador"}
										</span>
									</div>
									<span
										className={`mt-2 inline-flex rounded-full border px-2.5 py-0.5 text-xs font-medium ${resultBadge.className}`}
									>
										{resultBadge.label}
									</span>
									<Button
										type="button"
										variant="ghost"
										size="icon-xs"
										className="absolute top-2 right-2 text-medium-gray hover:text-destructive"
										aria-label="Eliminar entrevista"
										disabled={
											deletingInterviewId ===
											interview.entrevistaId
										}
										onClick={() =>
											void handleDeleteInterview(
												interview,
											)
										}
									>
										{deletingInterviewId ===
										interview.entrevistaId ? (
											<Loader2 className="animate-spin" />
										) : (
											<Trash2 />
										)}
									</Button>
								</article>
							);
						})}
					</div>

					{isAddFormOpen && (
						<form
							className="grid gap-3 rounded-md border border-light-gray bg-white p-3"
							onSubmit={handleCreateInterview}
						>
							<div className="grid gap-1">
								<label
									htmlFor="tipoEntrevista"
									className="text-xs text-medium-gray"
								>
									Tipo entrevista
								</label>
								<Select
									value={formValues.tipoEntrevista}
									onValueChange={(value) =>
										updateFormValue(
											"tipoEntrevista",
											value as InterviewType,
										)
									}
									disabled={isSubmittingInterview}
								>
									<SelectTrigger id="tipoEntrevista">
										<SelectValue />
									</SelectTrigger>
									<SelectContent>
										{interviewTypeOptions.map((option) => (
											<SelectItem
												key={option.value}
												value={option.value}
											>
												{option.label}
											</SelectItem>
										))}
									</SelectContent>
								</Select>
							</div>

							<div className="grid gap-1">
								<label
									htmlFor="fechaEntrevista"
									className="text-xs text-medium-gray"
								>
									Fecha entrevista
								</label>
								<Input
									id="fechaEntrevista"
									type="datetime-local"
									value={formValues.fechaEntrevista}
									onChange={(event) =>
										updateFormValue(
											"fechaEntrevista",
											event.target.value,
										)
									}
									disabled={isSubmittingInterview}
								/>
							</div>

							<div className="grid gap-1">
								<label
									htmlFor="entrevistador"
									className="text-xs text-medium-gray"
								>
									Entrevistador
								</label>
								<Input
									id="entrevistador"
									value={formValues.entrevistador}
									onChange={(event) =>
										updateFormValue(
											"entrevistador",
											event.target.value,
										)
									}
									disabled={isSubmittingInterview}
									placeholder="Steve Carr"
								/>
							</div>

							<div className="grid gap-1">
								<label
									htmlFor="resultadoEntrevista"
									className="text-xs text-medium-gray"
								>
									Resultado
								</label>
								<Select
									value={formValues.resultadoEntrevista}
									onValueChange={(value) =>
										updateFormValue(
											"resultadoEntrevista",
											value as InterviewResult,
										)
									}
									disabled={isSubmittingInterview}
								>
									<SelectTrigger id="resultadoEntrevista">
										<SelectValue />
									</SelectTrigger>
									<SelectContent>
										<SelectItem value="PENDIENTE">
											Pendiente
										</SelectItem>
										<SelectItem value="SUPERADA">
											Superada
										</SelectItem>
										<SelectItem value="FALLIDA">
											Fallida
										</SelectItem>
									</SelectContent>
								</Select>
							</div>

							{formError && (
								<p className="rounded-md border border-red-200 bg-red-50 p-2 text-sm text-red-700">
									{formError}
								</p>
							)}

							<div className="flex justify-end gap-2">
								<Button
									type="button"
									variant="ghost"
									disabled={isSubmittingInterview}
									onClick={() => {
										setIsAddFormOpen(false);
										setFormValues(initialInterviewValues());
										setFormError(null);
									}}
								>
									Cancelar
								</Button>
								<Button
									type="submit"
									disabled={isSubmittingInterview}
								>
									{isSubmittingInterview && (
										<Loader2 className="animate-spin" />
									)}
									Guardar
								</Button>
							</div>
						</form>
					)}

					<Button
						type="button"
						variant="ghost"
						className="w-full border border-dashed border-medium-gray bg-transparent text-medium-gray shadow-none hover:bg-off-white"
						onClick={() => setIsAddFormOpen(true)}
					>
						<Plus />
						Añadir Entrevista
					</Button>
				</section>

				<section className="grid w-full gap-2">
					<h3 className="text-left text-base font-normal text-medium-gray">
						Nota Postulacion
					</h3>
					<div className="min-h-24 w-full whitespace-pre-wrap rounded-md border border-light-gray bg-off-white p-4 text-left font-display text-sm text-black italic shadow-cool-light-inner">
						{postulation.notaPostulacion?.trim() ||
							"Sin nota registrada."}
					</div>
				</section>
			</DialogContent>
		</Dialog>
	);
}
