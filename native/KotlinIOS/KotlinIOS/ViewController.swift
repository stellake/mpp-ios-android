import UIKit
import SharedCode

class ViewController: UIViewController {

    @IBOutlet private var mainText: UILabel!
    @IBOutlet private var subHeader: UILabel!
    @IBOutlet private var picker: UIPickerView!
    @IBOutlet private var button: UIButton!
    @IBOutlet private var resultsTable: UITableView!
    
    private var pickerData: [String] = [String]()
    private var fromSelected: String!
    private var toSelected: String!
    private var journeyCollection: JourneyCollection!
    
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
        let alert = UIAlertController(title: "Error", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default))
        self.present(alert, animated: true, completion: nil)
    }
    
    func displayJourneys(journeyCollection: JourneyCollection) {
        self.journeyCollection = journeyCollection
        resultsTable.dataSource = self
        resultsTable.reloadData()
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
        picker.selectRow(1, inComponent: 1, animated: false)
    }
}

extension ViewController: UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 2
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerData.count
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

extension ViewController : UITableViewDataSource {
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "results_item", for: indexPath)
        
        cell.textLabel!.text = journeyCollection.outboundJourneys[indexPath.item].departureTime
        return cell
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return journeyCollection.outboundJourneys.count
    }
}
