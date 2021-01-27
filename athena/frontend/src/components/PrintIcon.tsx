import * as React from "react"
import printicon from "../resources/images/printicon.svg"

const PrintIcon = () => {
  return (
    <a className="print-link" href="#" onClick={window.print}>
      <img src={printicon}/>
    </a>
  )
}

export default PrintIcon
