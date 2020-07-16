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
    
    var departure_picker = UIPickerView()
    var arrival_picker = UIPickerView()
    var currentDeparture = ""
    var currentArrival = ""
    let toolBarDeparture = UIToolbar()
    let toolBarArrival = UIToolbar()
    let doneButtonDeparture = UIBarButtonItem(title: "Done", style: .plain, target: self, action: #selector(doneClickDeparture))
    let doneButtonArrival = UIBarButtonItem(title: "Done", style: .plain, target: self, action: #selector(doneClickArrival))
    let spaceButton = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
    let cancelButtonDeparture = UIBarButtonItem(title: "Cancel", style: .plain, target: self, action: #selector(cancelClick))
    let cancelButtonArrival = UIBarButtonItem(title: "Cancel", style: .plain, target: self, action: #selector(cancelClick))

    //Table variables
    
    private var tableContents:Array<String>=[]
    private let tableID="potato"
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    
    //On load
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: tableID)
        
        createpickers()
    }
    
    @IBAction func button_press(_ sender: Any) {
        presenter.onDoneButtonPressed()
    }
    
}



extension ViewController: ApplicationContractView {
    func updateStations(data: [String]) {
        print("")
    }
    
    
    func getArrivalDepartureStations() -> KotlinPair {
        
        let departureStation = departure_field.text
        
        let arrivalStation = arrival_field.text
        
        return KotlinPair(first: arrivalStation  ,second:departureStation)
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


//Picker Stuff

extension ViewController {
    func createpickers(){
        //New Departure Picker
        departure_field.inputView = departure_picker
        departure_picker.delegate=self
        departure_picker.dataSource=self
        toolBarDeparture.barStyle = .default
        toolBarDeparture.isTranslucent = true
        toolBarDeparture.setItems([cancelButtonDeparture, spaceButton, doneButtonDeparture], animated: false)
        toolBarDeparture.sizeToFit()
        toolBarDeparture.isUserInteractionEnabled = true
        departure_field.inputAccessoryView = toolBarDeparture
        
        //New Arrival Picker
        arrival_field.inputView = arrival_picker
        arrival_picker.delegate=self
        arrival_picker.dataSource=self
        toolBarArrival.barStyle = .default
        toolBarArrival.isTranslucent = true
        toolBarArrival.setItems([cancelButtonArrival, spaceButton, doneButtonArrival], animated: false)
        toolBarArrival.sizeToFit()
        toolBarArrival.isUserInteractionEnabled = true
        arrival_field.inputAccessoryView = toolBarArrival
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    // The number of rows of data
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return stations.count
    }
    
    // The data to return for the row and component (column) that's being passed in
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return stations[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        
        switch pickerView {
        case departure_picker:
            currentDeparture = stations[departure_picker.selectedRow(inComponent: 0)]
        case arrival_picker:
            currentArrival = stations[arrival_picker.selectedRow(inComponent: 0)]
        default:
            departure_field.resignFirstResponder()
            arrival_field.resignFirstResponder()
        }
    }
    
    @objc func cancelClick() {
        departure_field.resignFirstResponder()
        arrival_field.resignFirstResponder()
    }
    
    @objc func doneClickDeparture() {
        departure_field.text = currentDeparture
        departure_field.resignFirstResponder()
    }
    
    @objc func doneClickArrival() {
        arrival_field.text = currentArrival
        arrival_field.resignFirstResponder()
    }
}

