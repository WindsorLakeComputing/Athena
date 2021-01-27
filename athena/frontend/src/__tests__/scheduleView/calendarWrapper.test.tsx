import moment from "moment"
import React from "react"
import CalendarWrapper from "../../components/scheduleView/CalendarWrapper"
import {expect, render } from "../../testHelpers/importsHelper"

it("displays a DayPickerSingleDateController component", () => {

  const onDateChangeStub = () => null

  const {container} = render(<CalendarWrapper
    onDateChange={onDateChangeStub}
    date={moment()}/>)

  expect(container.querySelectorAll('[aria-label="Calendar"]').length).to.eq(1)
})
