#Plan of Action for Athena Application

##Key Dates: 
1. Kickoff Date: 02/11/2019
2. Inception Date: 02/20/2019
3. MVP Date: unknown

##Vision
We believe we can save section chiefs a large amount of time planning maintenance shifts
and processing certification checklists by creating a streamlined application to gather and act 
upon information. Additionally, with a data science component, we believe we can automate 
a large portion of resource planning and decrease the number of maintainers needed. 

##Strategy
We will develop an application which allows section chiefs to view and assign maintainers
to shifts. The application will also ingest suggested maintenance plans from the data science component
and display them to the user.

##Summary
 Athena ensures enough maintainers are working so F35s get fixed.

## Goals and Metrics
1. Provide a centralized location for staff planning
2. Replace external systems needed to perform resource planning

### Release Focus
- Allow a section chief to make and modify a daily staffing plan.

### Release Functionality
- Provides users a web-based view of their staff plan that allows users to edit.

### System Integration Considerations
- We hope integrate with other Mad Hatter applications in the future.

- There exists a system of record for AF training/certifications called IMDS, we are investigating
integrating with them but may decide deploy MVP without said integration