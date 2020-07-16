import UIKit
import SharedCode


class ViewController: UIViewController,UIPickerViewDataSource,UIPickerViewDelegate {

    @IBOutlet private var label: UILabel!
    
    @IBOutlet private var arrival_departure_button: UIButton!
    
    @IBOutlet private var tableView: UITableView!
    
    var stations=["Edinburgh Waverly","King's Cross","York","Durham","Cambridge"]
    
    
    //Creating new picker
    
    @IBOutlet private var departure_field: UITextField!
    
    @IBOutlet private var arrival_field: UITextField!
    
    var departure_picker2 = UIPickerView()
    var arrival_picker2 = UIPickerView()
    

    
    private var tableContents:Array<String>=[]
    private let tableID="potato"
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: tableID)
        //New Departure Picker
        departure_field.inputView = departure_picker2
        departure_picker2.delegate=self
        departure_picker2.dataSource=self
        
        //New Arrival Picker
        arrival_field.inputView = arrival_picker2
        arrival_picker2.delegate=self
        arrival_picker2.dataSource=self
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    
    //Picker Section
    
    // The number of rows of data
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return stations.count
    }
    
    // The data to return for the row and component (column) that's being passed in
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return stations[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        
        if pickerView == departure_picker2 {
        departure_field.text = stations[departure_picker2.selectedRow(inComponent: 0)]
            departure_field.resignFirstResponder()
        } else {
        arrival_field.text = stations[arrival_picker2.selectedRow(inComponent: 0)]
        arrival_field.resignFirstResponder()
        }
    }
    
    @IBAction func button_press(_ sender: Any) {
        presenter.onStationsSubmitted(departure: departure_field.text!, arrival: arrival_field.text!)
    }
    
}



extension ViewController: ApplicationContractView {
    func showAPIError(info: String) {
        //TODO - show message to user
        print(info)
    }
    
    func updateStations(data: [String]) {
        print("")
    }
    
    func openURL(url: String) {
        UIApplication.shared.open(URL(string: url)!)
    }
    
    func setLabel(text: String) {
        label.text = text
    }
}
extension ViewController: UITableViewDataSource,UITableViewDelegate{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableContents.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell=tableView.dequeueReusableCell(withIdentifier: tableID)!
        cell.textLabel?.text=tableContents[indexPath.row]
        return cell
    }
    func showData(data: [ApplicationContractTrainJourney]) {
        tableContents.removeAll()
        for journey in data{
            let currencyFormatter = NumberFormatter()
            currencyFormatter.usesGroupingSeparator = true
            currencyFormatter.numberStyle = .currency
            currencyFormatter.locale = Locale.init(identifier: "en_GB")
            let priceString = currencyFormatter.string(from: NSNumber(value: Double(journey.cost)/100.0))!
            tableContents.append(journey.departureTime+", "+journey.arrivalTime+" : "+priceString)
        }
        tableView.reloadData()
    }
    
}
