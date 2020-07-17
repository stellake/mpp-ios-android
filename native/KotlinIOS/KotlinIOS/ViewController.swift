import UIKit
import SharedCode

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet private var label: UILabel!

    @IBOutlet weak var DepartureLabel: UILabel!
    @IBOutlet weak var ArrivalLabel: UILabel!
    @IBOutlet weak var DurationLabel: UILabel!
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    private var data: [JourneyOption]?
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var Submit: UIButton!
    var pickerData:[String] = [String]()
    
    
    private func setUpTable() {
        let nib = UINib(nibName: "JourneyView", bundle: nil)
        tableView.register(nib, forCellReuseIdentifier: "CUSTOM_CELL")
        tableView.tableFooterView = UIView(frame: .zero)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        setUpTable()
        tableView.dataSource = self
        tableView.delegate = self
        self.view.addSubview(self.tableView)
        tableView.rowHeight = UITableView.automaticDimension
        tableView.estimatedRowHeight = 44
        DepartureLabel.isHidden = true
        ArrivalLabel.isHidden = true
        DurationLabel.isHidden = true
    }
    
    override func didReceiveMemoryWarning() {
            super.didReceiveMemoryWarning()
            // Dispose of any resources that can be recreated.
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
        let timeString = String(format: "%04u-%02u-%02uT%02u:%02u:00.000", year, month, day, hour, minutes+1)
        presenter.onButtonPressed(origin: outboundSelection, destination: inboundSelection, time: timeString )
        
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data?.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CUSTOM_CELL") as! JourneyView
        let journey = data?[indexPath.row]
        cell.setData(outboundCode: journey!.originStation.crs ,
                     inboundCode: journey!.destinationStation.crs ,
                     outMonth: journey!.departureTime.dateTime.month1 ,
                     outDay: journey!.departureTime.dateTime.dayOfMonth ,
                     outHour: journey!.departureTime.dateTime.hours ,
                     outMinute: journey!.departureTime.dateTime.minutes ,
                     arrivalTime: String(journey!.arrivalTime.dateTime.hours)+":"+String(journey!.arrivalTime.dateTime.minutes) ,
                     duration: String((journey!.arrivalTime.dateTime.minus(other: (journey!.departureTime.dateTime)))/60000),
                     delegate: self)
        return cell
    }
    
    
}


extension ViewController: ApplicationContractView {
   
    func showData(journeys: [JourneyOption]) {
        self.data = journeys
        DepartureLabel.isHidden = false
        ArrivalLabel.isHidden = false
        DurationLabel.isHidden = false
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

extension ViewController: CustomTableCellDelegate {
    
    func onBuyButtonTapped(outboundCode: String, inboundCode: String, outMonth: Int32, outDay: Int32, outHour: Int32, outMinute: Int32, returnSymbol: Bool = true) {
        presenter.onBuyButton(outbound: outboundCode, inbound: inboundCode, month: outMonth, day: outDay, hour: outHour, minutes: outMinute, returnBool: returnSymbol)    }
}
