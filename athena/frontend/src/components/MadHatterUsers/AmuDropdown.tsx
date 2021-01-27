import {Component, default as React} from "react"
import RequiredSelect from "../RequiredSelect"

class AmuDropdown extends Component<{
  selectedAmu: string | undefined,
  onAmuSelect: (value: string) => void
}> {

  private options = [
    {value: "Bolt", label: "Bolt"},
    {value: "Lightning", label: "Lightning"}
  ]

  constructor(props: any) {
    super(props)
  }

  public render() {
    return (
      <RequiredSelect id={"amu-dropdown"}
                      options={this.options}
                      onChange={this.props.onAmuSelect}
                      selected={this.props.selectedAmu}/>
    )
  }
}

export default AmuDropdown
