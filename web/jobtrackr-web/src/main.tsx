import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { createBrowserRouter } from "react-router";
import { RouterProvider } from "react-router";

const API_URL = import.meta.env.VITE_API_URL;

console.log(API_URL);

const getUserData = async () => {
	const response = await fetch(`${API_URL}/usuario`);

	if (!response.ok) throw response;

	return await response.json();
};

const getAllPostulations = async () => {
	const response = await fetch(`${API_URL}/postulaciones`);

	if (!response.ok) throw response;
	
	return await response.json();
}

const getAllEnterprises = async () => {
	const response = await fetch(`${API_URL}/empresas`);

	if (!response.ok) throw response;

	return await response.json();
};

const router = createBrowserRouter([
	{
		path: "/",
		element: <App />,
		loader: () => {
			const userData = getUserData();
			const postulations = getAllPostulations();
			const enterprises = getAllEnterprises();

			return {
				userDataPromise: userData,
				postulationsPromise: postulations,
				enterprisesPromise: enterprises,
			};
		},
	},
]);

createRoot(document.getElementById("root")!).render(
	<StrictMode>
		<RouterProvider router={router} />
	</StrictMode>,
);
