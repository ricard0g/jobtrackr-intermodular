import "./App.css";
import { KanbanBoard } from "./components/kanban/KanbanBoard";
import { Navbar } from "./components/shared/Navbar";

function App() {
	return (
		<section className="max-w-screen w-full max-h-screen h-auto overflow-hidden">
			<Navbar  />
      <main>
        <KanbanBoard />
      </main>
		</section>
	);
}

export default App;
