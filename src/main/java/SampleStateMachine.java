import java.io.InputStream;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IState;
import com.change_vision.jude.api.inf.model.IStateMachine;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.model.ITransition;
import com.change_vision.jude.api.inf.model.IVertex;
import com.change_vision.jude.api.inf.project.ModelFinder;
import com.change_vision.jude.api.inf.project.ProjectAccessor;


public class SampleStateMachine {
	
	public static void main(String[] args) throws Exception {
		AstahAPI api = AstahAPI.getAstahAPI();
		ProjectAccessor projectAccessor = api.getProjectAccessor();
		try {
			openSampleModel(projectAccessor);
			INamedElement[] foundElements = findStateMachine(projectAccessor);
			for (INamedElement element : foundElements) {
				IStateMachineDiagram stateMachineDiagram = castStateMachineDiagaram(element);
				if (stateMachineDiagram == null) {
					continue;
				}
				IStateMachine machine = stateMachineDiagram.getStateMachine();
				showStateMachine(machine);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			projectAccessor.close();
		}
	}

	/**
	 * サンプルモデルを開きます。
	 * @param projectAccessor
	 * @throws Exception
	 */
	private static void openSampleModel(ProjectAccessor projectAccessor)
			throws Exception {
		InputStream astahFileStream = SampleStateMachine.class.getResourceAsStream("Sample.asta");
		projectAccessor.open(astahFileStream);
	}

	/**
	 * Statemachine Diagramという名前の要素を取得します。
	 * @param projectAccessor
	 * @return 発見したモデル
	 * @throws Exception
	 */
	private static INamedElement[] findStateMachine(
			ProjectAccessor projectAccessor) throws Exception {
		INamedElement[] foundElements = projectAccessor.findElements(new ModelFinder() {
			public boolean isTarget(INamedElement namedElement) {
				return namedElement.getName().equals("Statemachine Diagram");
			}
		});
		return foundElements;
	}

	private static IStateMachineDiagram castStateMachineDiagaram(
			INamedElement element) {
		IStateMachineDiagram stateMachineDiagram = null;
		if (element instanceof IStateMachineDiagram) {
			stateMachineDiagram = (IStateMachineDiagram) element;
		}
		return stateMachineDiagram;
	}

	/**
	 * 状態マシンの要素を表示します。
	 * @see http://members.change-vision.com/javadoc/astah-api/6_7_0-43495/api/ja/doc/javadoc/com/change_vision/jude/api/inf/model/IStateMachine.html
	 * @param machine
	 */
	private static void showStateMachine(IStateMachine machine) {
		System.out.println("start ");
		showSeparator();
		showStates(machine);
		showSeparator();
		showVertexes(machine);
		showSeparator();
		showTransitions(machine);
		showSeparator();
		System.out.println("end.");
	}

	/**
	 * 状態マシンの呼び出し元の状態を取得します。
	 * @param machine
	 * @see http://members.change-vision.com/javadoc/astah-api/6_7_0-43495/api/ja/doc/javadoc/com/change_vision/jude/api/inf/model/IState.html
	 */
	private static void showStates(IStateMachine machine) {
		System.out.println("State start.");
		IState[] states = machine.getStates();
		for (IState state : states) {
			System.out.println(state);
		}
		System.out.println("State end.");
	}

	/**
	 * Vertexの集合を表示します。
	 * @param machine
	 */
	private static void showVertexes(IStateMachine machine) {
		System.out.println("Vertex start.");
		IVertex[] vertexes = machine.getVertexes();
		for (IVertex vertex : vertexes) {
			showVertex(vertex);
		}
		System.out.println("Vertex end.");
	}

	/**
	 * Vertex(頂点)の要素を表示します。
	 * @param vertex
	 * @see http://members.change-vision.com/javadoc/astah-api/6_7_0-43495/api/ja/doc/javadoc/com/change_vision/jude/api/inf/model/IVertex.html
	 */
	private static void showVertex(IVertex vertex) {
		showMiniSeparator();
		System.out.println("vertex : " + vertex);
		showMiniSeparator();
		showIncoming(vertex);
		showMiniSeparator();
		showOutgoing(vertex);
		if (vertex instanceof IState) {
			IState state = (IState) vertex;
			IVertex[] subvertexes = state.getSubvertexes();
			for (IVertex subvertex : subvertexes) {
				showMiniSeparator();
				System.out.println("found sub vertex");
				showVertex(subvertex);
				showMiniSeparator();
			}
			IStateMachine submachine = state.getSubmachine();
			if (submachine != null) {
				showMiniSeparator();
				System.out.println("found sub machine");
				showStateMachine(submachine);
			}
		}
		showMiniSeparator();
	}

	/**
	 * Vertexの入力側を表示します。
	 * @param vertex
	 */
	private static void showIncoming(IVertex vertex) {
		System.out.println("incoming start.");
		ITransition[] incomings = vertex.getIncomings();
		for (ITransition incoming : incomings) {
			System.out.println(incoming);
		}
		System.out.println("incoming end.");
	}

	/**
	 * Vertexの出力側を表示します。
	 * @param vertex
	 */
	private static void showOutgoing(IVertex vertex) {
		System.out.println("outgoing start.");
		ITransition[] getOutgoings = vertex.getOutgoings();
		for (ITransition outgoing : getOutgoings) {
			System.out.println(outgoing);
		}
		System.out.println("outgoing end.");
	}

	/**
	 * 状態マシン内の遷移を取得します。
	 * @param machine
	 * @see http://members.change-vision.com/javadoc/astah-api/6_7_0-43495/api/ja/doc/javadoc/com/change_vision/jude/api/inf/model/ITransition.html
	 */
	private static void showTransitions(IStateMachine machine) {
		System.out.println("Transition start.");
		ITransition[] transitions = machine.getTransitions();
		for (ITransition transition : transitions) {
			System.out.println("transition : " + transition);
		}
		System.out.println("Transition end.");
	}

	private static void showMiniSeparator() {
		System.out.println("----");
	}

	private static void showSeparator() {
		System.out.println("-----------------------");
	}

}
