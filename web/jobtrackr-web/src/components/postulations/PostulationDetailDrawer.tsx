import { Loader2, Pencil, Plus, Tags, Trash2, X } from "lucide-react";
import {
	type FormEvent,
	type ReactNode,
	useCallback,
	useEffect,
	useMemo,
	useState,
} from "react";

import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import {
	Dialog,
	DialogClose,
	DialogContent,
	DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
	Select,
	SelectContent,
	SelectItem,
	SelectTrigger,
	SelectValue,
} from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import type {
	CreateInterviewRequest,
	Interview,
	InterviewResult,
	InterviewType,
} from "@/types/interview";
import type {
	CreatePostulationRequest,
	Postulation,
} from "@/types/postulation";
import { postulationStatus } from "@/types/postulationStatus";
import type { Tag } from "@/types/tag";

const API_URL = import.meta.env.VITE_API_URL;

interface PostulationDetailDrawerProps {
	open: boolean;
	onOpenChange: (open: boolean) => void;
	postulation: Postulation | null;
	getNextOrderKanban: (estatus: string, postulacionId: number) => number;
	onPostulationUpdated: (postulation: Postulation) => void;
	onPostulationDeleted: (postulacionId: number) => void;
}

type DrawerMode = "view" | "edit" | "tags";

type InterviewFormValues = {
	tipoEntrevista: InterviewType;
	fechaEntrevista: string;
	entrevistador: string;
	resultadoEntrevista: InterviewResult;
};

type PostulationFormValues = {
	rol: string;
	estatus: string;
	salarioMinimo: string;
	salarioMaximo: string;
	ubicacion: string;
	esTelematico: boolean;
	ofertaUrl: string;
	notaPostulacion: string;
	fechaPostulacion: string;
};

type PostulationFormErrors = Partial<
	Record<keyof PostulationFormValues, string>
>;

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

const initialPostulationValues = (
	postulation: Postulation | null,
): PostulationFormValues => ({
	rol: postulation?.rol ?? "",
	estatus: postulation?.estatus ?? "POSTULADA",
	salarioMinimo: postulation?.salarioMinimo?.toString() ?? "",
	salarioMaximo: postulation?.salarioMaximo?.toString() ?? "",
	ubicacion: postulation?.ubicacion ?? "",
	esTelematico: postulation?.esTelematico ?? false,
	ofertaUrl: postulation?.ofertaUrl ?? "",
	notaPostulacion: postulation?.notaPostulacion ?? "",
	fechaPostulacion: postulation?.fechaPostulacion ?? "",
});

const toNullableString = (value: string) => {
	const trimmedValue = value.trim();

	return trimmedValue.length > 0 ? trimmedValue : null;
};

const toNullableNumber = (value: string) => {
	const trimmedValue = value.trim();

	return trimmedValue.length > 0 ? Number(trimmedValue) : null;
};

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
	children: ReactNode;
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
	getNextOrderKanban,
	onPostulationUpdated,
	onPostulationDeleted,
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
	const [mode, setMode] = useState<DrawerMode>("view");
	const [postulationValues, setPostulationValues] =
		useState<PostulationFormValues>(() => initialPostulationValues(null));
	const [postulationErrors, setPostulationErrors] =
		useState<PostulationFormErrors>({});
	const [postulationServerError, setPostulationServerError] = useState<
		string | null
	>(null);
	const [isSubmittingPostulation, setIsSubmittingPostulation] =
		useState(false);
	const [isDeletingPostulation, setIsDeletingPostulation] = useState(false);
	const [allTags, setAllTags] = useState<Tag[]>([]);
	const [hasLoadedTags, setHasLoadedTags] = useState(false);
	const [selectedTagIds, setSelectedTagIds] = useState<Set<number>>(
		() => new Set(),
	);
	const [isLoadingTags, setIsLoadingTags] = useState(false);
	const [tagError, setTagError] = useState<string | null>(null);
	const [isSubmittingTags, setIsSubmittingTags] = useState(false);

	const statusDisplay = useMemo(
		() => getStatusDisplay(postulation?.estatus ?? ""),
		[postulation?.estatus],
	);

	const statusOptions = useMemo(
		() =>
			postulationStatus.map(([label]) => ({
				label,
				value: label.toUpperCase(),
			})),
		[],
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

	const resetDrawerState = useCallback(() => {
		setInterviews([]);
		setInterviewError(null);
		setIsAddFormOpen(false);
		setFormValues(initialInterviewValues());
		setFormError(null);
		setDeletingInterviewId(null);
		setMode("view");
		setPostulationValues(initialPostulationValues(postulation));
		setPostulationErrors({});
		setPostulationServerError(null);
		setIsSubmittingPostulation(false);
		setIsDeletingPostulation(false);
		setSelectedTagIds(
			new Set(postulation?.tagList.map((tag) => tag.etiquetaId) ?? []),
		);
		setTagError(null);
		setIsSubmittingTags(false);
	}, [postulation]);

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
	}, [open, postulation, resetDrawerState]);

	useEffect(() => {
		if (!open || mode !== "tags" || hasLoadedTags) return;

		const controller = new AbortController();

		const fetchTags = async () => {
			setIsLoadingTags(true);
			setTagError(null);

			try {
				const response = await fetch(`${API_URL}/etiquetas`, {
					signal: controller.signal,
				});

				if (!response.ok) {
					throw new Error(
						await readApiError(
							response,
							"No se pudieron cargar las etiquetas.",
						),
					);
				}

				const data = (await response.json()) as Tag[];

				setAllTags(data);
				setHasLoadedTags(true);
			} catch (error) {
				if (error instanceof DOMException && error.name === "AbortError") {
					return;
				}

				setTagError(
					error instanceof Error
						? error.message
						: "No se pudieron cargar las etiquetas.",
				);
			} finally {
				setIsLoadingTags(false);
			}
		};

		void fetchTags();

		return () => controller.abort();
	}, [hasLoadedTags, mode, open]);

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

	const updatePostulationValue = <T extends keyof PostulationFormValues>(
		name: T,
		value: PostulationFormValues[T],
	) => {
		setPostulationValues((currentValues) => ({
			...currentValues,
			[name]: value,
		}));
		setPostulationErrors((currentErrors) => ({
			...currentErrors,
			[name]: undefined,
		}));
		setPostulationServerError(null);
	};

	const buildPostulationPayload = () => {
		const nextErrors: PostulationFormErrors = {};
		const salarioMinimo = toNullableNumber(
			postulationValues.salarioMinimo,
		);
		const salarioMaximo = toNullableNumber(
			postulationValues.salarioMaximo,
		);

		if (!postulationValues.rol.trim()) {
			nextErrors.rol = "Indica el rol de la oferta.";
		}

		if (!postulationValues.estatus) {
			nextErrors.estatus = "Selecciona un estatus.";
		}

		if (!postulationValues.fechaPostulacion) {
			nextErrors.fechaPostulacion =
				"Indica la fecha de postulacion.";
		}

		if (
			salarioMinimo !== null &&
			(!Number.isFinite(salarioMinimo) || salarioMinimo <= 0)
		) {
			nextErrors.salarioMinimo = "Debe ser un numero mayor que 0.";
		}

		if (
			salarioMaximo !== null &&
			(!Number.isFinite(salarioMaximo) || salarioMaximo <= 0)
		) {
			nextErrors.salarioMaximo = "Debe ser un numero mayor que 0.";
		}

		if (
			salarioMinimo !== null &&
			salarioMaximo !== null &&
			Number.isFinite(salarioMinimo) &&
			Number.isFinite(salarioMaximo) &&
			salarioMaximo <= salarioMinimo
		) {
			nextErrors.salarioMaximo =
				"Debe ser mayor que el salario minimo.";
		}

		setPostulationErrors(nextErrors);

		if (Object.keys(nextErrors).length > 0) {
			return null;
		}

		const ordenKanban =
			postulationValues.estatus === postulation.estatus
				? postulation.ordenKanban
				: getNextOrderKanban(
						postulationValues.estatus,
						postulation.postulacionId,
					);

		return {
			usuarioId: postulation.usuarioId,
			empresaId: postulation.empresaId,
			rol: postulationValues.rol.trim(),
			estatus: postulationValues.estatus,
			ordenKanban,
			salarioMinimo,
			salarioMaximo,
			ubicacion: toNullableString(postulationValues.ubicacion),
			esTelematico: postulationValues.esTelematico,
			ofertaUrl: toNullableString(postulationValues.ofertaUrl),
			notaPostulacion: toNullableString(
				postulationValues.notaPostulacion,
			),
			fechaPostulacion: postulationValues.fechaPostulacion,
		} satisfies CreatePostulationRequest;
	};

	const handleEnterEditMode = () => {
		setMode("edit");
		setPostulationValues(initialPostulationValues(postulation));
		setPostulationErrors({});
		setPostulationServerError(null);
	};

	const handleEnterTagMode = () => {
		setMode("tags");
		setSelectedTagIds(
			new Set(postulation.tagList.map((tag) => tag.etiquetaId)),
		);
		setTagError(null);
	};

	const handleUpdatePostulation = async () => {
		const payload = buildPostulationPayload();

		if (!payload) return;

		setIsSubmittingPostulation(true);
		setPostulationServerError(null);

		try {
			const response = await fetch(
				`${API_URL}/postulaciones/${postulation.postulacionId}`,
				{
					method: "PUT",
					headers: {
						"Content-Type": "application/json",
					},
					body: JSON.stringify(payload),
				},
			);

			if (!response.ok) {
				throw new Error(
					await readApiError(
						response,
						"No se pudo actualizar la postulacion.",
					),
				);
			}

			onPostulationUpdated({
				...postulation,
				...payload,
			});
			setMode("view");
		} catch (error) {
			setPostulationServerError(
				error instanceof Error
					? error.message
					: "No se pudo actualizar la postulacion.",
			);
		} finally {
			setIsSubmittingPostulation(false);
		}
	};

	const handleDeletePostulation = async () => {
		const confirmed = window.confirm(
			"¿Eliminar esta postulacion? Esta accion no se puede deshacer.",
		);

		if (!confirmed) return;

		setIsDeletingPostulation(true);
		setPostulationServerError(null);

		try {
			const response = await fetch(
				`${API_URL}/postulaciones/${postulation.postulacionId}`,
				{ method: "DELETE" },
			);

			if (!response.ok) {
				throw new Error(
					await readApiError(
						response,
						"No se pudo eliminar la postulacion.",
					),
				);
			}

			onPostulationDeleted(postulation.postulacionId);
		} catch (error) {
			setPostulationServerError(
				error instanceof Error
					? error.message
					: "No se pudo eliminar la postulacion.",
			);
		} finally {
			setIsDeletingPostulation(false);
		}
	};

	const toggleSelectedTag = (tagId: number) => {
		setSelectedTagIds((currentTagIds) => {
			const nextTagIds = new Set(currentTagIds);

			if (nextTagIds.has(tagId)) {
				nextTagIds.delete(tagId);
			} else {
				nextTagIds.add(tagId);
			}

			return nextTagIds;
		});
		setTagError(null);
	};

	const handleUpdateTags = async () => {
		setIsSubmittingTags(true);
		setTagError(null);

		const tagIds = Array.from(selectedTagIds);

		try {
			const response = await fetch(
				`${API_URL}/postulaciones/${postulation.postulacionId}/etiquetas`,
				{
					method: "PUT",
					headers: {
						"Content-Type": "application/json",
					},
					body: JSON.stringify(tagIds),
				},
			);

			if (!response.ok) {
				throw new Error(
					await readApiError(
						response,
						"No se pudieron actualizar las etiquetas.",
					),
				);
			}

			onPostulationUpdated({
				...postulation,
				tagList: allTags.filter((tag) =>
					selectedTagIds.has(tag.etiquetaId),
				),
			});
			setMode("view");
		} catch (error) {
			setTagError(
				error instanceof Error
					? error.message
					: "No se pudieron actualizar las etiquetas.",
			);
		} finally {
			setIsSubmittingTags(false);
		}
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

				{mode === "edit" ? (
					<div
						className="grid w-full gap-4 pb-24"
					>
						<div className="grid gap-4 sm:grid-cols-2">
							<div className="grid gap-2">
								<Label htmlFor="postulation-rol">Rol</Label>
								<Input
									id="postulation-rol"
									value={postulationValues.rol}
									onChange={(event) =>
										updatePostulationValue(
											"rol",
											event.target.value,
										)
									}
									aria-invalid={Boolean(
										postulationErrors.rol,
									)}
									disabled={isSubmittingPostulation}
									maxLength={150}
								/>
								{postulationErrors.rol && (
									<p className="text-sm text-destructive">
										{postulationErrors.rol}
									</p>
								)}
							</div>

							<div className="grid gap-2">
								<Label htmlFor="postulation-estatus">
									Estatus
								</Label>
								<Select
									value={postulationValues.estatus}
									onValueChange={(value) =>
										updatePostulationValue(
											"estatus",
											value,
										)
									}
									disabled={isSubmittingPostulation}
								>
									<SelectTrigger
										id="postulation-estatus"
										aria-invalid={Boolean(
											postulationErrors.estatus,
										)}
									>
										<SelectValue />
									</SelectTrigger>
									<SelectContent>
										{statusOptions.map((status) => (
											<SelectItem
												key={status.value}
												value={status.value}
											>
												{status.label}
											</SelectItem>
										))}
									</SelectContent>
								</Select>
								{postulationErrors.estatus && (
									<p className="text-sm text-destructive">
										{postulationErrors.estatus}
									</p>
								)}
							</div>
						</div>

						<div className="grid gap-4 sm:grid-cols-2">
							<div className="grid gap-2">
								<Label htmlFor="postulation-salario-minimo">
									Salario minimo
								</Label>
								<Input
									id="postulation-salario-minimo"
									type="number"
									value={postulationValues.salarioMinimo}
									onChange={(event) =>
										updatePostulationValue(
											"salarioMinimo",
											event.target.value,
										)
									}
									aria-invalid={Boolean(
										postulationErrors.salarioMinimo,
									)}
									disabled={isSubmittingPostulation}
									min="0"
									step="0.01"
								/>
								{postulationErrors.salarioMinimo && (
									<p className="text-sm text-destructive">
										{postulationErrors.salarioMinimo}
									</p>
								)}
							</div>

							<div className="grid gap-2">
								<Label htmlFor="postulation-salario-maximo">
									Salario maximo
								</Label>
								<Input
									id="postulation-salario-maximo"
									type="number"
									value={postulationValues.salarioMaximo}
									onChange={(event) =>
										updatePostulationValue(
											"salarioMaximo",
											event.target.value,
										)
									}
									aria-invalid={Boolean(
										postulationErrors.salarioMaximo,
									)}
									disabled={isSubmittingPostulation}
									min="0"
									step="0.01"
								/>
								{postulationErrors.salarioMaximo && (
									<p className="text-sm text-destructive">
										{postulationErrors.salarioMaximo}
									</p>
								)}
							</div>
						</div>

						<div className="grid gap-4 sm:grid-cols-[1fr_auto] sm:items-end">
							<div className="grid gap-2">
								<Label htmlFor="postulation-ubicacion">
									Ubicacion
								</Label>
								<Input
									id="postulation-ubicacion"
									value={postulationValues.ubicacion}
									onChange={(event) =>
										updatePostulationValue(
											"ubicacion",
											event.target.value,
										)
									}
									disabled={isSubmittingPostulation}
									maxLength={100}
								/>
							</div>

							<div className="flex h-9 items-center gap-2">
								<Checkbox
									id="postulation-es-telematico"
									checked={postulationValues.esTelematico}
									onCheckedChange={(checked) =>
										updatePostulationValue(
											"esTelematico",
											checked === true,
										)
									}
									disabled={isSubmittingPostulation}
								/>
								<Label htmlFor="postulation-es-telematico">
									Telematico
								</Label>
							</div>
						</div>

						<div className="grid gap-2">
							<Label htmlFor="postulation-oferta-url">
								URL de la oferta
							</Label>
							<Input
								id="postulation-oferta-url"
								type="url"
								value={postulationValues.ofertaUrl}
								onChange={(event) =>
									updatePostulationValue(
										"ofertaUrl",
										event.target.value,
									)
								}
								disabled={isSubmittingPostulation}
								maxLength={500}
							/>
						</div>

						<div className="grid gap-2">
							<Label htmlFor="postulation-fecha">
								Fecha de postulacion
							</Label>
							<Input
								id="postulation-fecha"
								type="date"
								value={postulationValues.fechaPostulacion}
								onChange={(event) =>
									updatePostulationValue(
										"fechaPostulacion",
										event.target.value,
									)
								}
								aria-invalid={Boolean(
									postulationErrors.fechaPostulacion,
								)}
								disabled={isSubmittingPostulation}
							/>
							{postulationErrors.fechaPostulacion && (
								<p className="text-sm text-destructive">
									{postulationErrors.fechaPostulacion}
								</p>
							)}
						</div>

						<div className="grid gap-2">
							<Label htmlFor="postulation-nota">Nota</Label>
							<Textarea
								id="postulation-nota"
								value={postulationValues.notaPostulacion}
								onChange={(event) =>
									updatePostulationValue(
										"notaPostulacion",
										event.target.value,
									)
								}
								disabled={isSubmittingPostulation}
							/>
						</div>

						{postulationServerError && (
							<p className="rounded-md border border-red-200 bg-red-50 p-3 text-sm text-red-700">
								{postulationServerError}
							</p>
						)}
					</div>
				) : mode === "tags" ? (
					<div
						className="grid w-full gap-3 pb-24"
					>
						<h3 className="text-left text-base font-normal text-medium-gray">
							Etiquetas
						</h3>

						{isLoadingTags && (
							<div className="flex items-center gap-2 rounded-md border border-light-gray bg-off-white p-3 text-sm text-medium-gray">
								<Loader2 className="size-4 animate-spin" />
								Cargando etiquetas
							</div>
						)}

						{tagError && (
							<p className="rounded-md border border-red-200 bg-red-50 p-3 text-sm text-red-700">
								{tagError}
							</p>
						)}

						{!isLoadingTags && !tagError && allTags.length === 0 && (
							<p className="rounded-md border border-light-gray bg-off-white p-3 text-sm text-medium-gray">
								Todavia no hay etiquetas disponibles.
							</p>
						)}

						<div className="grid max-h-[62dvh] gap-2 overflow-y-auto pr-1">
							{allTags.map((tag) => {
								const checked = selectedTagIds.has(
									tag.etiquetaId,
								);

								return (
									<div
										key={tag.etiquetaId}
										className="flex min-h-10 cursor-pointer items-center gap-3 rounded-md border bg-off-white px-3 py-2 text-sm transition-colors"
										style={{
											borderColor: checked
												? tag.colorEtiqueta
												: undefined,
											backgroundColor: checked
												? `${tag.colorEtiqueta}22`
												: undefined,
										}}
									>
										<Checkbox
											id={`tag-${tag.etiquetaId}`}
											checked={checked}
											onCheckedChange={() =>
												toggleSelectedTag(
													tag.etiquetaId,
												)
											}
											disabled={isSubmittingTags}
										/>
										<span
											className="size-3 rounded-full"
											style={{
												backgroundColor:
													tag.colorEtiqueta,
											}}
										/>
										<Label
											htmlFor={`tag-${tag.etiquetaId}`}
											className="min-w-0 cursor-pointer truncate text-sm font-normal text-black"
										>
											{tag.nombreEtiqueta}
										</Label>
									</div>
								);
							})}
						</div>
					</div>
				) : (
					<>
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
					</>
				)}

				<footer className="sticky right-0 bottom-0 left-0 -mx-4 mt-auto flex w-[calc(100%+2rem)] justify-end gap-2 border-t border-light-gray bg-white/95 p-4 shadow-cool-light backdrop-blur">
					{mode === "edit" ? (
						<>
							<Button
								type="button"
								variant="ghost"
								disabled={isSubmittingPostulation}
								onClick={() => {
									setMode("view");
									setPostulationValues(
										initialPostulationValues(postulation),
									);
									setPostulationErrors({});
									setPostulationServerError(null);
								}}
							>
								Cancelar
							</Button>
							<Button
								type="button"
								disabled={isSubmittingPostulation}
								onClick={() => void handleUpdatePostulation()}
							>
								{isSubmittingPostulation && (
									<Loader2 className="animate-spin" />
								)}
								Guardar
							</Button>
						</>
					) : mode === "tags" ? (
						<>
							<Button
								type="button"
								variant="ghost"
								disabled={isSubmittingTags}
								onClick={() => {
									setMode("view");
									setSelectedTagIds(
										new Set(
											postulation.tagList.map(
												(tag) => tag.etiquetaId,
											),
										),
									);
									setTagError(null);
								}}
							>
								Cancelar
							</Button>
							<Button
								type="button"
								disabled={isSubmittingTags || isLoadingTags}
								onClick={() => void handleUpdateTags()}
							>
								{isSubmittingTags && (
									<Loader2 className="animate-spin" />
								)}
								Guardar etiquetas
							</Button>
						</>
					) : (
						<>
							{postulationServerError && (
								<p className="mr-auto self-center text-sm text-destructive">
									{postulationServerError}
								</p>
							)}
							<Button
								type="button"
								variant="ghost"
								disabled={isDeletingPostulation}
								onClick={handleEnterEditMode}
							>
								<Pencil />
								Editar
							</Button>
							<Button
								type="button"
								variant="ghost"
								disabled={isDeletingPostulation}
								onClick={handleEnterTagMode}
							>
								<Tags />
								Etiquetas
							</Button>
							<Button
								type="button"
								variant="destructive"
								disabled={isDeletingPostulation}
								onClick={() => void handleDeletePostulation()}
							>
								{isDeletingPostulation ? (
									<Loader2 className="animate-spin" />
								) : (
									<Trash2 />
								)}
								Eliminar
							</Button>
						</>
					)}
				</footer>
			</DialogContent>
		</Dialog>
	);
}
