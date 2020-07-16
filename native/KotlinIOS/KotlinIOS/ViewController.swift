import UIKit
import SharedCode

enum pickerType{
    case arrival
    case departure
}
class ViewController: UIViewController,UIPickerViewDataSource,UIPickerViewDelegate {

    @IBOutlet private var label: UILabel!
    
    @IBOutlet private var arrival_departure_button: UIButton!
    
    @IBOutlet private var tableView: UITableView!
    
    var stations=["Amersham"] //blatant nepotism
    
    
    @IBOutlet private var departure_field: UITextField!
    
    @IBOutlet private var arrival_field: UITextField!
    
    var pickerState=pickerType.arrival
    
    //Creating new picker
    var commonPicker = UIPickerView()
    
    var currentText = ""
    let commonToolBar = UIToolbar()
    let commonDoneButton = UIBarButtonItem(title: "Done", style: .plain, target: self, action: #selector(doneClick))
    let flexSpace = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
    let commonCancelButton = UIBarButtonItem(title: "Cancel", style: .plain, target: self, action: #selector(cancelClick))
    
    var departureStations = [String]()
    var arrivalStations = [String]()

    //Table variables

    
    private var tableContents:Array<String>=[]
    private let tableID="potato"
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    //On load
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: tableID)
        createPickers()
        
    }
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }
    
    @IBAction func button_press(_ sender: Any) {
        presenter.onDoneButtonPressed()
    }
    
}



extension ViewController: ApplicationContractView {
    func updateStations(data: [String]) {
        stations=data
        updatePickers()
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
        if data.count==0 {
            tableContents.append("No tickets found :(")
        }else{
            for journey in data{
                let currencyFormatter = NumberFormatter()
                currencyFormatter.usesGroupingSeparator = true
                currencyFormatter.numberStyle = .currency
                currencyFormatter.locale = Locale.init(identifier: "en_GB")
                let priceString = currencyFormatter.string(from: NSNumber(value: Double(journey.cost)/100.0))!
                tableContents.append(journey.departureTime+", "+journey.arrivalTime+" : "+priceString)
            }
        }
        tableView.reloadData()
    }
    
}


//Picker Stuff

extension ViewController {
    func updateState(_ sender:Any){
        pickerState = (sender as AnyObject===departure_field) ? pickerType.departure : pickerType.arrival
    }
    @IBAction func stationValueBeginsEditing(_ sender: Any) {
        updateState(sender)
        stationFilter()
        updatePickers()
    }
    
    @IBAction func stationValueChanges(_ sender: Any) {
        stationValueBeginsEditing(sender)
    }
    func createPickers(){
        //New Common Picker
        commonPicker.delegate=self
        commonPicker.dataSource=self
        commonToolBar.barStyle = .default
        commonToolBar.tintColor = .red
        commonToolBar.isTranslucent = true
        commonToolBar.setItems([commonCancelButton, flexSpace, commonDoneButton], animated: false)
        commonToolBar.sizeToFit()
    }
    func updatePickers(){
        //Do some amount of createPickers...
        let field=currentField()
        field.inputView = commonPicker
        commonToolBar.isUserInteractionEnabled = true
        field.inputAccessoryView = commonToolBar
        commonPicker.reloadAllComponents()
    }
    private func currentField()->UITextField{
        return pickerState==pickerType.arrival ? arrival_field : departure_field
    }
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    // The number of rows of data
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        switch pickerState {
        case pickerType.departure:
            return departureStations.count
        case pickerType.arrival:
            return arrivalStations.count
        }
    }
    
    // The data to return for the row and component (column) that's being passed in
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        switch pickerState {
        case pickerType.departure:
            return departureStations[row]
        case pickerType.arrival:
            return arrivalStations[row]
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        
        switch pickerState {
        case pickerType.departure:
            currentText = departureStations[commonPicker.selectedRow(inComponent: 0)]
        case pickerType.arrival:
            currentText = arrivalStations[commonPicker.selectedRow(inComponent: 0)]
        }
    }
    
    @objc func cancelClick() {
        currentField().resignFirstResponder()
    }
    @objc func doneClick(){
        let field = currentField()
        field.text=currentText
        field.resignFirstResponder()
    }
    
    //Search function
    
    
    
    //Filters out stations
    
    func stationFilter() {
        let field=currentField()
        var currentStations = [String]()
        if field.text == "" {
            currentStations=stations
        } else {
            let searchInput = field.text!
            currentStations=stations.filter({ $0.lowercased().prefix(searchInput.count) == searchInput.lowercased()})
        }
        
        if currentStations.count > 0 {
            currentText = currentStations[0]
        } else {
            currentText = ""
        }
        if (pickerState==pickerType.arrival){
            arrivalStations=currentStations
        }else{
            departureStations=currentStations
        }
    }
}

