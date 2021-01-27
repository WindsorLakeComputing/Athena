import * as React from "react"
import {Maintainer} from "../models/Maintainer"
import checkmark from "../resources/images/checkmark.png"
import {expect, fireEvent} from "../testHelpers/importsHelper"

export const buildCertificateColumns = (maintainer: Maintainer, certificateHeaders: string[]) => (
  certificateHeaders.map((certificate, index) => {
    if (maintainer.certificates.find((maintainerCertificate) => maintainerCertificate.name === certificate)) {
      return (<td key={index}><img src={checkmark} alt="hasCert"/></td>)
    } else {
      return (<td key={index}></td>)
    }
  })
)

export const selectFromDropdown = async (selectComponent: Element, option: string) => {
  await fireEvent.focus(selectComponent.querySelectorAll("input")[0])
  const selectBox = selectComponent.querySelectorAll(".react-select__control")[0]
  await fireEvent.mouseDown(selectBox)
  const menuItems = selectComponent.querySelectorAll(".react-select__option")
  await menuItems.forEach(async (menuItem) => {
    if (menuItem.innerHTML === option) {
      await fireEvent.click(menuItem)
    }
  })
  await fireEvent.blur(selectComponent.querySelectorAll("input")[0])
}
