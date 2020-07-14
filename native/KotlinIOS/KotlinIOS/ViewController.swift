import UIKit
import SharedCode


class ViewController: UIViewController,UIPickerViewDataSource,UIPickerViewDelegate {

    @IBOutlet private var label: UILabel!
    
    @IBOutlet private var departure_picker: UIPickerView!
    @IBOutlet private var arrival_picker: UIPickerView!
    
    @IBOutlet private var arrival_departure_button: UIButton!
    
    var stations=["Edinburgh Waverly","King's Cross","York","Durham","Cambridge"]
    
    private var tableContents:Array<String>=[]
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
    
    @IBAction func button_press(_ sender: Any) {
        presenter.onDoneButtonPressed()
    }
    
}



extension ViewController: ApplicationContractView {
    func getArrivalDepartureStations() -> KotlinPair {
        
        let departureStation = stations[departure_picker.selectedRow(inComponent: 0)]
        
        let arrivalStation = stations[arrival_picker.selectedRow(inComponent: 0)]
        
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
        <#code#>
    }
    func showData(data: [ApplicationContractTrainJourney]) {
        tableContents.removeAll()
        for journey in data{
            tableContents.append(journey.departureTime+", "+journey.arrivalTime)
        }
    }
    
}
