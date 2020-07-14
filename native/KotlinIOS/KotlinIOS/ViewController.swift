import UIKit
import SharedCode

class ViewController: UIViewController {

    @IBOutlet private var label: UILabel!
    var data = ["HLB", "TON", "CHX", "SEV", "LDB"]

    @IBOutlet private var pickerdeparture: UIPickerView!
    @IBOutlet private var pickerdestination: UIPickerView!
    
    //var departureStation = data[0]
    //var destinationStation = data[0]


    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        pickerdeparture.dataSource = self
        pickerdeparture.delegate = self
        pickerdestination.dataSource = self
        pickerdestination.delegate = self
        
        presenter.onViewTaken(view: self)
    }
    @IBAction func ButtonPress(_ sender: Any) {
        let depart = data[pickerdeparture.selectedRow(inComponent: 0)]
        let dest = data[pickerdestination.selectedRow(inComponent: 0)]
        
        let string = presenter.getTimesRequest(departure: depart, destination: dest)
        if let url = URL(string: string) {
            UIApplication.shared.open(url)
        }
    }
}

extension ViewController: UIPickerViewDelegate, UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    

    // 3 items for the picker.

    // Outlet.

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    private func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
        // Column count: use one column.
        return 1
    }

    internal func pickerView(_ pickerView: UIPickerView,
        numberOfRowsInComponent component: Int) -> Int {

            // Row count: rows equals array length.
            return data.count
    }

    internal func pickerView(_ pickerView: UIPickerView,
        titleForRow row: Int,
        forComponent component: Int) -> String? {

            // Return a string from the array for this row.
            return data[row]
    }
    
}

extension ViewController: ApplicationContractView {
    func setLabel(text: String) {
        label.text = text
    }
}
