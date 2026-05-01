import { CirclePlus, Loader2 } from "lucide-react";
import { type FormEvent, useMemo, useState } from "react";
import { useRevalidator } from "react-router";

import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import {
	Dialog,
	DialogContent,
	DialogDescription,
	DialogFooter,
	DialogHeader,
	DialogTitle,
	DialogTrigger,
} from "@/components/ui/dialog";
import {
	Form,
	FormControl,
	FormField,
	FormLabel,
	FormMessage,
} from "@/components/ui/form";
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
import type { Enterprise } from "@/types/enterprise";
import type {
	CreatePostulationRequest,
	Postulation,
} from "@/types/postulation";
import { postulationStatus } from "@/types/postulationStatus";
import type { User } from "@/types/user";

const API_URL = import.meta.env.VITE_API_URL;

type CreatePostulationFormValues = {
	empresaId: string;
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

type FormErrors = Partial<Record<keyof CreatePostulationFormValues, string>>;

interface CreatePostulationDialogProps {
	user: User;
	enterprises: Enterprise[];
	postulations: Postulation[];
}

const getTodayInputValue = () => {
	const today = new Date();
	const year = today.getFullYear();
	const month = String(today.getMonth() + 1).padStart(2, "0");
	const day = String(today.getDate()).padStart(2, "0");

	return `${year}-${month}-${day}`;
};

const initialFormValues = (): CreatePostulationFormValues => ({
	empresaId: "",
	rol: "",
	estatus: "POSTULADA",
	salarioMinimo: "",
	salarioMaximo: "",
	ubicacion: "",
	esTelematico: false,
	ofertaUrl: "",
	notaPostulacion: "",
	fechaPostulacion: getTodayInputValue(),
});

const toNullableString = (value: string) => {
	const trimmedValue = value.trim();

	return trimmedValue.length > 0 ? trimmedValue : null;
};

const toNullableNumber = (value: string) => {
	const trimmedValue = value.trim();

	return trimmedValue.length > 0 ? Number(trimmedValue) : null;
};

const readApiError = async (response: Response) => {
	try {
		const body = (await response.json()) as { error?: string };

		return body.error ?? "No se pudo crear la postulacion.";
	} catch {
		return "No se pudo crear la postulacion.";
	}
};

export function CreatePostulationDialog({
	user,
	enterprises,
	postulations,
}: CreatePostulationDialogProps) {
	const revalidator = useRevalidator();
	const [open, setOpen] = useState(false);
	const [values, setValues] = useState<CreatePostulationFormValues>(() =>
		initialFormValues(),
	);
	const [errors, setErrors] = useState<FormErrors>({});
	const [serverError, setServerError] = useState<string | null>(null);
	const [isSubmitting, setIsSubmitting] = useState(false);

	const statusOptions = useMemo(
		() =>
			postulationStatus.map(([label]) => ({
				label,
				value: label.toUpperCase(),
			})),
		[],
	);

	const updateValue = <T extends keyof CreatePostulationFormValues>(
		name: T,
		value: CreatePostulationFormValues[T],
	) => {
		setValues((currentValues) => ({
			...currentValues,
			[name]: value,
		}));
		setErrors((currentErrors) => ({
			...currentErrors,
			[name]: undefined,
		}));
		setServerError(null);
	};

	const resetForm = () => {
		setValues(initialFormValues());
		setErrors({});
		setServerError(null);
	};

	const buildPayload = () => {
		const nextErrors: FormErrors = {};
		const salarioMinimo = toNullableNumber(values.salarioMinimo);
		const salarioMaximo = toNullableNumber(values.salarioMaximo);

		if (!values.empresaId) {
			nextErrors.empresaId = "Selecciona una empresa.";
		}

		if (!values.rol.trim()) {
			nextErrors.rol = "Indica el rol de la oferta.";
		}

		if (!values.estatus) {
			nextErrors.estatus = "Selecciona un estatus.";
		}

		if (!values.fechaPostulacion) {
			nextErrors.fechaPostulacion = "Indica la fecha de postulacion.";
		}

		if (salarioMinimo !== null && (!Number.isFinite(salarioMinimo) || salarioMinimo <= 0)) {
			nextErrors.salarioMinimo = "Debe ser un numero mayor que 0.";
		}

		if (salarioMaximo !== null && (!Number.isFinite(salarioMaximo) || salarioMaximo <= 0)) {
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

		setErrors(nextErrors);

		if (Object.keys(nextErrors).length > 0) {
			return null;
		}

		const ordenKanban = postulations.filter(
			(postulation) => postulation.estatus === values.estatus,
		).length;

		return {
			usuarioId: user.usuarioId,
			empresaId: Number(values.empresaId),
			rol: values.rol.trim(),
			estatus: values.estatus,
			ordenKanban,
			salarioMinimo,
			salarioMaximo,
			ubicacion: toNullableString(values.ubicacion),
			esTelematico: values.esTelematico,
			ofertaUrl: toNullableString(values.ofertaUrl),
			notaPostulacion: toNullableString(values.notaPostulacion),
			fechaPostulacion: values.fechaPostulacion,
		} satisfies CreatePostulationRequest;
	};

	const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
		event.preventDefault();
		const payload = buildPayload();

		if (!payload) return;

		setIsSubmitting(true);
		setServerError(null);

		try {
			const response = await fetch(`${API_URL}/postulaciones`, {
				method: "POST",
				body: JSON.stringify(payload),
			});

			if (!response.ok) {
				throw new Error(await readApiError(response));
			}

			resetForm();
			setOpen(false);
			void revalidator.revalidate();
		} catch (error) {
			setServerError(
				error instanceof Error
					? error.message
					: "No se pudo crear la postulacion.",
			);
		} finally {
			setIsSubmitting(false);
		}
	};

	return (
		<Dialog
			open={open}
			onOpenChange={(nextOpen) => {
				setOpen(nextOpen);
				if (!nextOpen) resetForm();
			}}
		>
			<DialogTrigger asChild>
				<Button size="lg" variant="default">
					<CirclePlus /> Crear Postulacion
				</Button>
			</DialogTrigger>
			<DialogContent>
				<DialogHeader>
					<DialogTitle>Crear postulacion</DialogTitle>
					<DialogDescription>
						Registra los datos principales de la oferta para anadirla
						al tablero.
					</DialogDescription>
				</DialogHeader>

				<Form className="grid gap-4" onSubmit={handleSubmit}>
					<div className="grid gap-4 sm:grid-cols-2">
						<FormField name="empresaId">
							<FormLabel>Empresa</FormLabel>
							<Select
								value={values.empresaId}
								onValueChange={(value) =>
									updateValue("empresaId", value)
								}
								disabled={isSubmitting}
							>
								<SelectTrigger
									aria-invalid={Boolean(errors.empresaId)}
								>
									<SelectValue placeholder="Seleccionar empresa" />
								</SelectTrigger>
								<SelectContent>
									{enterprises.map((enterprise) => (
										<SelectItem
											key={enterprise.empresaId}
											value={String(enterprise.empresaId)}
										>
											{enterprise.nombreEmpresa}
										</SelectItem>
									))}
								</SelectContent>
							</Select>
							{errors.empresaId && (
								<p className="text-sm text-destructive">
									{errors.empresaId}
								</p>
							)}
						</FormField>

						<FormField name="estatus">
							<FormLabel>Estatus</FormLabel>
							<Select
								value={values.estatus}
								onValueChange={(value) =>
									updateValue("estatus", value)
								}
								disabled={isSubmitting}
							>
								<SelectTrigger
									aria-invalid={Boolean(errors.estatus)}
								>
									<SelectValue placeholder="Seleccionar estatus" />
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
							{errors.estatus && (
								<p className="text-sm text-destructive">
									{errors.estatus}
								</p>
							)}
						</FormField>
					</div>

					<FormField name="rol">
						<FormLabel>Rol</FormLabel>
						<FormControl asChild>
							<Input
								value={values.rol}
								onChange={(event) =>
									updateValue("rol", event.target.value)
								}
								aria-invalid={Boolean(errors.rol)}
								disabled={isSubmitting}
								maxLength={150}
								placeholder="Junior Frontend Developer"
							/>
						</FormControl>
						{errors.rol && <FormMessage>{errors.rol}</FormMessage>}
					</FormField>

					<div className="grid gap-4 sm:grid-cols-2">
						<FormField name="salarioMinimo">
							<FormLabel>Salario minimo</FormLabel>
							<FormControl asChild>
								<Input
									type="number"
									value={values.salarioMinimo}
									onChange={(event) =>
										updateValue(
											"salarioMinimo",
											event.target.value,
										)
									}
									aria-invalid={Boolean(errors.salarioMinimo)}
									disabled={isSubmitting}
									min="0"
									step="0.01"
									placeholder="20000"
								/>
							</FormControl>
							{errors.salarioMinimo && (
								<FormMessage>
									{errors.salarioMinimo}
								</FormMessage>
							)}
						</FormField>

						<FormField name="salarioMaximo">
							<FormLabel>Salario maximo</FormLabel>
							<FormControl asChild>
								<Input
									type="number"
									value={values.salarioMaximo}
									onChange={(event) =>
										updateValue(
											"salarioMaximo",
											event.target.value,
										)
									}
									aria-invalid={Boolean(errors.salarioMaximo)}
									disabled={isSubmitting}
									min="0"
									step="0.01"
									placeholder="26000"
								/>
							</FormControl>
							{errors.salarioMaximo && (
								<FormMessage>
									{errors.salarioMaximo}
								</FormMessage>
							)}
						</FormField>
					</div>

					<div className="grid gap-4 sm:grid-cols-[1fr_auto] sm:items-end">
						<FormField name="ubicacion">
							<FormLabel>Ubicacion</FormLabel>
							<FormControl asChild>
								<Input
									value={values.ubicacion}
									onChange={(event) =>
										updateValue(
											"ubicacion",
											event.target.value,
										)
									}
									disabled={isSubmitting}
									maxLength={100}
									placeholder="Madrid, Espana"
								/>
							</FormControl>
						</FormField>

						<div className="flex h-9 items-center gap-2">
							<Checkbox
								id="esTelematico"
								checked={values.esTelematico}
								onCheckedChange={(checked) =>
									updateValue(
										"esTelematico",
										checked === true,
									)
								}
								disabled={isSubmitting}
							/>
							<Label htmlFor="esTelematico">Telematico</Label>
						</div>
					</div>

					<FormField name="ofertaUrl">
						<FormLabel>URL de la oferta</FormLabel>
						<FormControl asChild>
							<Input
								type="url"
								value={values.ofertaUrl}
								onChange={(event) =>
									updateValue("ofertaUrl", event.target.value)
								}
								disabled={isSubmitting}
								maxLength={500}
								placeholder="https://..."
							/>
						</FormControl>
					</FormField>

					<FormField name="fechaPostulacion">
						<FormLabel>Fecha de postulacion</FormLabel>
						<FormControl asChild>
							<Input
								type="date"
								value={values.fechaPostulacion}
								onChange={(event) =>
									updateValue(
										"fechaPostulacion",
										event.target.value,
									)
								}
								aria-invalid={Boolean(
									errors.fechaPostulacion,
								)}
								disabled={isSubmitting}
							/>
						</FormControl>
						{errors.fechaPostulacion && (
							<FormMessage>{errors.fechaPostulacion}</FormMessage>
						)}
					</FormField>

					<FormField name="notaPostulacion">
						<FormLabel>Nota</FormLabel>
						<FormControl asChild>
							<Textarea
								value={values.notaPostulacion}
								onChange={(event) =>
									updateValue(
										"notaPostulacion",
										event.target.value,
									)
								}
								disabled={isSubmitting}
								placeholder="Detalles del proceso, contacto o requisitos relevantes"
							/>
						</FormControl>
					</FormField>

					{serverError && (
						<p className="rounded-lg border border-destructive/30 bg-destructive/10 px-3 py-2 text-sm text-destructive">
							{serverError}
						</p>
					)}

					<DialogFooter>
						<Button
							type="button"
							variant="ghost"
							onClick={() => setOpen(false)}
							disabled={isSubmitting}
						>
							Cancelar
						</Button>
						<Button type="submit" disabled={isSubmitting}>
							{isSubmitting && (
								<Loader2 className="animate-spin" />
							)}
							Crear
						</Button>
					</DialogFooter>
				</Form>
			</DialogContent>
		</Dialog>
	);
}
