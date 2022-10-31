import SwiftUI
import shared

struct ContentView: View {
	var body: some View {
    	ZStack {
            ComposeController()
    	}
    }
}

struct ComposeController: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> some UIViewController {
        ComposeRootControllerKt.getRootController()
    }

    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        uiViewController.view.setNeedsLayout()
    }
}
