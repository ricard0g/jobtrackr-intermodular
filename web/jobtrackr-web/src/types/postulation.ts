import type { Enterprise } from "./enterprise";
import type { Tag } from "./tag";

export interface Postulation {
    postulacionId: number;
    usuarioId: number;
    empresaId: number;
    rol: string;
    estatus: string;
    ordenKanban: number;
    salarioMinimo: number;
    salarioMaximo: number;
    ubicacion: string;
    esTelematico: boolean;
    ofertaUrl: string;
    creadaEn: string;
    actualizadaEn: string;
    notaPostulacion: string;
    fechaPostulacion: string;
    tagList: Tag[];
    empresa: Enterprise;
}

export interface CreatePostulationRequest {
	usuarioId: number;
	empresaId: number;
	rol: string;
	estatus: string;
	ordenKanban: number;
	salarioMinimo: number | null;
	salarioMaximo: number | null;
	ubicacion: string | null;
	esTelematico: boolean;
	ofertaUrl: string | null;
	notaPostulacion: string | null;
	fechaPostulacion: string;
}
