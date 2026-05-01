export type InterviewType =
	| "PERSONAL"
	| "TELEFONICA"
	| "TECNICA"
	| "ARQUITECTURA"
	| "RRHH"
	| "FINAL";

export type InterviewResult = "PENDIENTE" | "SUPERADA" | "FALLIDA";

export interface Interview {
	entrevistaId: number;
	postulacionId: number;
	numeroRonda: number;
	tipoEntrevista: InterviewType;
	fechaEntrevista: string;
	entrevistador: string;
	resultadoEntrevista: InterviewResult;
}

export interface CreateInterviewRequest {
	postulacionId: number;
	numeroRonda: number;
	tipoEntrevista: InterviewType;
	fechaEntrevista: string;
	entrevistador: string;
	resultadoEntrevista: InterviewResult;
}
