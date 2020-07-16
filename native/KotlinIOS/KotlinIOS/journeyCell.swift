//
//  journeyCell.swift
//  KotlinIOS
//
//  Created by Elliot Barnes on 16/07/2020.
//  Copyright © 2020 Evgeny Petrenko. All rights reserved.
//

import Foundation
import UIKit
import SharedCode

class journeyCell: UITableViewCell {
    @IBOutlet weak var DepartureTimeLabel: UILabel!
    @IBOutlet weak var ArrivalTimeLabel: UILabel!
    @IBOutlet weak var DurationLabel: UILabel!
    @IBOutlet weak var BuyButton: UIButton!
    
    private var delegate: CustomTableCellDelegate?
    private var outMonth: String = ""
    private var outDay: String = ""
    private var outHour: String = ""
    private var outMinute: String = ""
    private var outboundCode: String = ""
    private var inboundCode: String = ""
    
    func setData(outboundCode: String, inboundCode: String, outMonth: String, outDay:String, outHour:String, outMinute:String,  arrivalTime: String, duration: String, delegate: CustomTableCellDelegate) {
        self.outboundCode = outboundCode
        self.inboundCode = inboundCode
        self.outMonth = outMonth
        self.outDay = outDay
        self.outHour = outHour
        self.outMinute = outMinute
        DepartureTimeLabel.text = outHour+":"+outMinute
        ArrivalTimeLabel.text = arrivalTime
        DurationLabel.text = duration
        self.delegate = delegate
    }
    
    @IBAction private func onBuyButtonTapped() {
        delegate?.onBuyButtonTapped(outboundCode: self.outboundCode, inboundCode: self.inboundCode, outMonth: self.outMonth, outDay: self.outDay, outHour: self.outHour, outMinute: self.outMinute, returnSymbol: "y")
    }
}

protocol CustomTableCellDelegate: AnyObject {
    func onBuyButtonTapped(outboundCode: String, inboundCode: String, outMonth: String,
                           outDay: String, outHour: String, outMinute: String, returnSymbol: String)
}
