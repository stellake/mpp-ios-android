import UIKit
import SharedCode

class ViewController: UIViewController {

    @IBOutlet private var label: UILabel!
    @IBOutlet private var spinner: UIPickerView!

    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
    }
}

extension ViewController: ApplicationContractView {
    func setFromSpinnerContent(list: [String]) {
        <#code#>
    }
    
    func setLabel(main_text: String, sub_header: String) {
        <#code#>
    }
    
    func setToSpinnerContent(list: [String]) {
        <#code#>
    }
    
    func setLabel(text: String) {
        label.text = text
    }
}
