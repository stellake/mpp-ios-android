import UIKit
import SharedCode

class ViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {

    @IBOutlet private var label: UILabel!

    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    private var data: [JourneyOption]?
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var outboundStationPicker: UIPickerView!
    @IBOutlet weak var inboundStationPicker: UIPickerView!
    @IBOutlet weak var Submit: UIButton!
    var pickerData:[String] = [String]()
    
    
    private func setUpTable() {
        let nib = UINib(nibName: "journeyCell", bundle: nil)
        tableView.register(nib, forCellReuseIdentifier: "CUSTOM_CELL")
        tableView.tableFooterView = UIView(frame: .zero)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        pickerData = ["Harrow and Wealdstone", "London Euston", "Canley", "Coventry", "Birmingham New Street"]
        outboundStationPicker.dataSource = self
        outboundStationPicker.delegate = self
        outboundStationPicker.dataSource = self
        outboundStationPicker.delegate = self
        setUpTable()
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
        
        
    }
    
    
}


extension ViewController: ApplicationContractView {
   
    func showData(journeys: [JourneyOption]) {
        
    }
    
    func showAlert(text: String) {
    
    }
    
    func setLabel(text: String) {
        label.text = text
    }
    
    func openWebpage(url: String) {
        
    }
}

extension ViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data?.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CUSTOM_CELL") as! journeyCell
        let thisCellRow = indexPath.row
        cell.setData(outboundCode: data?[thisCellRow].originStation.crs ?? "" , inboundCode: data?[thisCellRow].destinationStation.crs ?? "", outMonth: data?[thisCellRow].outboundMonth, outDay: <#T##String#>, outHour: <#T##String#>, outMinute: <#T##String#>, arrivalTime: <#T##String#>, duration: <#T##String#>, delegate: <#T##CustomTableCellDelegate#>)
    }
}

extension ViewController: CustomTableCellDelegate {
    func onBuyButtonTapped(outboundCode: String, inboundCode: String, outMonth: String, outDay: String, outHour: String, outMinute: String, returnSymbol: String) {
        presenter.onBuyButton(outboundCode,inboundCode, outMonth, outDay, outHour, outMinute, "y")
    }
}
