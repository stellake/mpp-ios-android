import UIKit
import SharedCode

class ViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {

    @IBOutlet private var label: UILabel!

    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    @IBOutlet weak var Station_picker1: UIPickerView!
    var pickerData:[String] = [String]()

    @IBOutlet weak var Submit: UIButton!
    @IBOutlet weak var Station_picker2: UIPickerView!
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        pickerData = ["Harrow and Wealdstone", "London Euston", "Canley", "Coventry", "Birmingham New Street"]
        Station_picker1.dataSource = self
        Station_picker1.delegate = self
        Station_picker2.dataSource = self
        Station_picker2.delegate = self
    }
    override func didReceiveMemoryWarning() {
            super.didReceiveMemoryWarning()
            // Dispose of any resources that can be recreated.
        }

        // Number of columns of data
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
            return 1
        }
        
        // The number of rows of data
        func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
            return pickerData.count
        }
        
        // The data to return fopr the row and component (column) that's being passed in
        func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
            return pickerData[row]
        }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent  component: Int) {
        let yearValueSelected = pickerData[row] as String
        print(yearValueSelected)
    }
    
    @IBAction func onButtonPress(_ sender: Any) {
        let origin: String = pickerData[Station_picker1.selectedRow(inComponent: 0)]
        let destination = pickerData[Station_picker2.selectedRow(inComponent: 0)]
        let url = (URL(string: presenter.onButtonPressed(origin: origin, destination: destination)) ?? URL(string: "https://lner.co.uk"))!
        UIApplication.shared.open(url, options: [:], completionHandler: nil)
        
    }
    
    
}

extension ViewController: ApplicationContractView {
    func showAlert(text: String) {
    
    }
    
    func setLabel(text: String) {
        label.text = text
    }
}
