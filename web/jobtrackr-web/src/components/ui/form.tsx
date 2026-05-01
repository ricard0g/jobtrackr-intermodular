import * as React from "react";
import { Form as FormPrimitive } from "radix-ui";

import { cn } from "@/lib/utils";

function Form({ ...props }: React.ComponentProps<typeof FormPrimitive.Root>) {
	return <FormPrimitive.Root data-slot="form" {...props} />;
}

function FormField({
	className,
	...props
}: React.ComponentProps<typeof FormPrimitive.Field>) {
	return (
		<FormPrimitive.Field
			data-slot="form-field"
			className={cn("grid gap-2", className)}
			{...props}
		/>
	);
}

function FormLabel({
	className,
	...props
}: React.ComponentProps<typeof FormPrimitive.Label>) {
	return (
		<FormPrimitive.Label
			data-slot="form-label"
			className={cn("text-sm font-medium", className)}
			{...props}
		/>
	);
}

function FormControl({
	...props
}: React.ComponentProps<typeof FormPrimitive.Control>) {
	return <FormPrimitive.Control data-slot="form-control" {...props} />;
}

function FormMessage({
	className,
	...props
}: React.ComponentProps<typeof FormPrimitive.Message>) {
	return (
		<FormPrimitive.Message
			data-slot="form-message"
			className={cn("text-sm text-destructive", className)}
			{...props}
		/>
	);
}

export { Form, FormControl, FormField, FormLabel, FormMessage };
