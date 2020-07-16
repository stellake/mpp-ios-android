import UIKit
import SharedCode

class ViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {

    @IBOutlet private var label: UILabel!

    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    private var data: [JourneyOption]?
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var Submit: UIButton!
    var pickerData:[String] = [String]()
    
    
    private func setUpTable() {
        let nib = UINib(nibName: "journeyCell", bundle: nil)
        tableView.register(nib, forCellReuseIdentifier: "CUSTOM_CELL")
        tableView.tableFooterView = UIView(frame: .zero)
        print("Hi")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
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
        let outboundSelection = "Birmingham New Street"
        let inboundSelection = "London Euston"
        let date = Date()
        let calendar = Calendar.current
        let year = calendar.component(.year, from: date)
        let month = calendar.component(.month, from: date)
        let day = calendar.component(.day, from: date)
        let hour = calendar.component(.hour, from: date)
        let minutes = calendar.component(.minute, from: date)
        let timeString = String(format: "%04u-%02u-%02uT%02u:%02u:00.000+00:00", year, month, day, hour, minutes)
        presenter.onButtonPressed(origin: outboundSelection, destination: inboundSelection, time: timeString)
        print(timeString)
    }
    
    
}


extension ViewController: ApplicationContractView {
   
    func showData(journeys: [JourneyOption]) {
        print("hi")
        data = journeys
        tableView.reloadData()
    }
    
    func showAlert(text: String) {
        print(text)
        setLabel(text: text)
    }
    
    func setLabel(text: String) {
        label.text = text
    }
    
    func openWebpage(url: String) {
        UIApplication.shared.open(NSURL(string:url)! as URL)
    }
}

extension ViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data?.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CUSTOM_CELL") as! journeyCell
        let thisCellRow = indexPath.row
        //cell.setData(outboundCode: data?[thisCellRow].originStation.crs ?? "" , inboundCode: data?[thisCellRow].destinationStation.crs ?? "", outMonth: data?[thisCellRow].outboundMonth ?? "", outDay: <#T##String#>, outHour: <#T##String#>, outMinute: <#T##String#>, arrivalTime: <#T##String#>, duration: <#T##String#>, delegate: self)
        cell.setData(outboundCode: "", inboundCode: "" , outMonth: 0, outDay: 0, outHour: 0, outMinute: 0, arrivalTime: "", duration: "", delegate: self)
        
        return cell
    }
}

extension ViewController: CustomTableCellDelegate {
    
    func onBuyButtonTapped(outboundCode: String, inboundCode: String, outMonth: Int32, outDay: Int32, outHour: Int32, outMinute: Int32, returnSymbol: Bool = true) {
        presenter.onBuyButton(outbound: outboundCode, inbound: inboundCode, month: outMonth, day: outDay, hour: outHour, minutes: outMinute, returnBool: returnSymbol)    }
}
