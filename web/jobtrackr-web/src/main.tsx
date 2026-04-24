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

const router = createBrowserRouter([
	{
		path: "/",
		element: <App />,
		loader: () => {
			const userData = getUserData();

			return {
				userDataPromise: userData,
			};
		},
	},
]);

createRoot(document.getElementById("root")!).render(
	<StrictMode>
		<RouterProvider router={router} />
	</StrictMode>,
);
