import React, { useEffect } from "react";
import Button from "@mui/material/Button";
import { PayModal } from "./PayModal"
import { AllReviews } from "./AllReviews";
export const Footer = (props) => {
  useEffect(() => {
    console.log(props);
  });
  return (
    <>
      {!props?.hasDeadlinePassed() && props?.isEventNotFull() && (
        <div
          style={{
            position: "fixed",
            bottom: 0,
            height: "8rem",
            width: "100%",
            border: "2px solid #000",
            boxShadow: "24",
            zIndex: "100",
            background: "white",
          }}
        >
          <div
            style={{
              display: "grid",
              gridGap: "10px",
              justifyContent: "end",
              gridTemplateRows: "3rem 3rem 2rem",
              margin: "1rem",
            }}
          >
            <AllReviews id={props?.orgId} forOrg={true}/>
            {props.fee !== null && props.fee !== 0 && (
              <div
                style={{
                  display: "inline-flex",
                  alignItems: "center",
                  gridRow: 2
                }}
              >
                <h3 style={{ margin: "20px" }}>Fee ${props.fee}</h3>
                <PayModal
                  fee={props.fee}
                  policy={props.admPolicy}
                  setPaid={props.setPaid}
                  paid={props.paid}
                />
              </div>
            )}
            {(props.fee === null || props?.fee === 0) && <h3>FREE</h3>}
            <Button
              sx={{ gridRow: 2 }}
              size="small"
              variant="contained"
              onClick={props.handleSubmit}
              disabled={!props?.status.includes('OPEN')}
            >
              {props.buttonLabel}
            </Button>
          </div>
        </div>
      )}
    </>
  );
}