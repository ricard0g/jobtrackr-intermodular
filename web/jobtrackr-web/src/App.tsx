import "./App.css";
import type { User } from "./types/user";
import { Navbar } from "./components/shared/Navbar";

interface LoaderData {
	userDataPromise: Promise<User>;
}

function App() {
	return (
		<section className="max-w-screen w-full h-auto">
			<Navbar  />
		</section>
	);
}

export default App;
