import type { User as UserModel } from "@/types/user";
import { CirclePlus, User, X } from "lucide-react";
import { use, useState } from "react";
import { Button } from "../ui/button";
import { useLoaderData } from "react-router";

interface NavbarLoaderData {
	userDataPromise: Promise<UserModel>;
}

export function Navbar() {
	const [openUserData, setOpenUserData] = useState(false);
	const { userDataPromise } = useLoaderData<NavbarLoaderData>();
	const user = use(userDataPromise);

	return (
		<header className="max-w-1/2 mx-auto my-4">
			<nav className="w-full py-2 px-4 bg-off-white border border-light-gray shadow-cool-light rounded-lg">
				<ul className="flex justify-between items-center w-full">
					<li className="relative flex items-center gap-x-2 text-sm">
						<Button
							onClick={() => setOpenUserData(!openUserData)}
							variant="ghost"
							className="p-2 rounded-lg hover:bg-light-gray"
						>
							<User />
						</Button>
						Bienvenido {user.primerNombreUsuario}!
						{openUserData && (
							<dl className="absolute top-14 flex flex-col gap-y-2 max-w-96 w-auto bg-off-white py-2 px-4 border border-light-gray shadow-cool-light rounded-lg">
								<Button
									onClick={() =>
										setOpenUserData(!openUserData)
									}
									variant="ghost"
									className="absolute right-2 top-2 w-6 h-6 p-2 rounded-lg hover:bg-light-gray"
								>
									<X />
								</Button>

								<div className="w-full">
									<dt className="text-sm text-medium-gray">
										Nombre Completo Usuario
									</dt>
									<dd className="font-medium">{`${user.primerNombreUsuario} ${user.segundoNombreUsuario ? user.segundoNombreUsuario : ""} ${user.primerApellidoUsuario} ${user.segundoApellidoUsuario}`}</dd>
								</div>

								<div className="w-full">
									<dt className="text-sm text-medium-gray">
										Correo Electronico
									</dt>
									<dd className="font-medium">
										{user.correoElectronicoUsuario}
									</dd>
								</div>
							</dl>
						)}
					</li>
					<li>
						<Button size="lg" variant="default">
							<CirclePlus /> Crear Postulacion
						</Button>
					</li>
				</ul>
			</nav>
		</header>
	);
}
