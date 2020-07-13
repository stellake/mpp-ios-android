import UIKit
import SharedCode


class ViewController: UIViewController,UIPickerViewDataSource,UIPickerViewDelegate {

    @IBOutlet private var label: UILabel!
    
    @IBOutlet private var departure_picker: UIPickerView!
    @IBOutlet private var arrival_picker: UIPickerView!
    
    var stations=["Edinburgh Waverly","King's Cross","York","Durham","Cambridge"]
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        departure_picker.delegate = self
        departure_picker.dataSource = self
        arrival_picker.delegate=self
        arrival_picker.dataSource=self
    }
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    // The number of rows of data
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return stations.count
    }
    
    // The data to return fopr the row and component (column) that's being passed in
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return stations[row]
    }
}

extension ViewController: ApplicationContractView {
    
    func getArrivalDepartureStations() -> KotlinPair {
        return KotlinPair(first:"King's Cross",second:"King's Cross")
    }
    
    func openURL(url: String) {
        
    }
    
    func setLabel(text: String) {
        label.text = text
    }
}
