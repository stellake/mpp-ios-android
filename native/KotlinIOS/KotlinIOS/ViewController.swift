import UIKit
import SharedCode

class ViewController: UIViewController {

    private var data: [String] = []

    @IBOutlet private var pickerdeparture: UIPickerView!
    @IBOutlet private var pickerdestination: UIPickerView!

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
        let depart = presenter.getStationCode(name: data[pickerdeparture.selectedRow(inComponent: 0)])
        let dest = presenter.getStationCode(name: data[pickerdestination.selectedRow(inComponent: 0)])
        
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
        // TODO: nothing
    }
    
    func updateDropDowns() {
        data = presenter.getStationNames().sorted()
        pickerdeparture.reloadAllComponents()
        pickerdestination.reloadAllComponents()
    }
}
