import type { PostulationStatusEnum } from "./postulationStatus";
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
}