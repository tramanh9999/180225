graph TD
A[Start evaluating approval process] --> B{Is the list of ApprovalRoles null or empty?};

    B -- Yes --> C[Result: PENDING_APPROVAL];

    B -- No --> D{Initialize flags:<br/>- allApprovalModelsAreNull = true<br/>- hasPendingOrNullIndividualStatus = false};
    D --> E{Loop through each ChangeRequestRoleUserModel};

    E -- Current roleUser.approvalModel.status is REJECTED? --> F[Result: REJECT];

    E -- No REJECTED found in current roleUser --> G{Is current roleUser.approvalModel null?};
    G -- Yes --> H[Set hasPendingOrNullIndividualStatus = true];
    G -- No --> I{Set allApprovalModelsAreNull = false};
    I --> J{Is current roleUser.approvalModel.status null or PENDING?};
    J -- Yes --> K[Set hasPendingOrNullIndividualStatus = true];

    K --> E;
    H --> E;
    J -- No --> E;

    E -- Loop Ends (No REJECT found) --> L{Are ALL ApprovalModels still null?};
    L -- Yes --> M[Result: TOBE_SENT];

    L -- No --> N{Is hasPendingOrNullIndividualStatus true?};
    N -- Yes --> O[Result: PENDING_APPROVAL];
    N -- No --> P[Result: ACCEPT];