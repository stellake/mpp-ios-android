import UIKit
import SharedCode

class ViewController: UIViewController {

    @IBOutlet private var mainText: UILabel!
    @IBOutlet private var subHeader: UILabel!
    @IBOutlet private var picker: UIPickerView!
    @IBOutlet private var button: UIButton!
    
    private var pickerData: [String] = [String]()
    private var fromSelected: String!
    private var toSelected: String!
    
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
    }
    
    @IBAction func sendSelection() {
        presenter.runSearch(from: fromSelected, to: toSelected)
    }
}

extension ViewController: ApplicationContractView {
    func displayErrorMessage(message: String) {
        
    }
    
    func displayJourneys(journeyCollection: JourneyCollection) {
        
    }
    
    func openUrl(url: String) {
        if let page = URL(string: url) {
            UIApplication.shared.open(page)
        }
    }
        
    func setTitle(title: String, subtitle: String) {
        mainText.text = title
        subHeader.text = subtitle
    }
    
    func setStations(stations: [String]) {
        pickerData = stations
        fromSelected = stations[0]
        toSelected = stations[1]
        picker.dataSource = self
        picker.delegate = self
    }
}

extension ViewController: UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 2
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return 5
    }
}

extension ViewController: UIPickerViewDelegate {
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerData[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        switch component {
        case 0: fromSelected = pickerData[row]
        case 1: toSelected = pickerData[row]
        default: break
        }
    }
}
