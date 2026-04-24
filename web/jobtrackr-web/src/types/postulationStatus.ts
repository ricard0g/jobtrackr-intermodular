export const PostulationStatus = {
	POSTULADA: ["Postulada", "#5765BD"] as const, // 'as const' makes this a readonly tuple, interesting really
	REVISION: ["Revision", "#F5D800"] as const,
	ENTREVISTA: ["Entrevista", "#00C0F5"] as const,
	OFERTA: ["Oferta", "#00F57E"] as const,
	DESCARTADA: ["Descartada", "#878787"] as const,
	RETIRADA: ["Retirada", "#BEAC8A"] as const,
} as const;

export enum PostulationStatusEnum {
	POSTULADA,
	REVISION,
	ENTREVISTA,
	OFERTA,
	DESCARTADA,
	RETIRADA,
}
