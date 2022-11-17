import React from "react";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import Modal from "@mui/material/Modal";
import TextField from "@mui/material/TextField";
import TaskAltIcon from "@mui/icons-material/TaskAlt";
const style = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 800,
    bgcolor: "background.paper",
    border: "2px solid #000",
    boxShadow: 24,
    pt: 2,
    px: 4,
    pb: 3,
};
export const PayModal = (props) => {
    const [open, setOpen] = React.useState(false);
    const [disabled, setDisabled] = React.useState(true);
    const [creditCard, setcreditCard] = React.useState(null);
    const handleOpen = () => {
        if (!props.paid) setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
    };

    const handlePay = (e) => {
        setDisabled(false);
        props.setPaid();
        handleClose();
    };
    return (<>
        {!props.paid && (<Button onClick={handleOpen}
            variant="outlined"
            sx={
                { marginRight: 3 }} >
            Are you willing to pay ?
        </Button>
        )
        } {
            props.paid && (
                <div>
                    <TaskAltIcon />
                </div>
            )
        }

        <Modal
            hideBackdrop open={open}
            onClose={handleClose}
            aria-labelledby="pay-modal">
            <Box sx={
                { ...style, width: 700 }} >
                <h4 id="pay-modal"> Fee: $ {props.fee} </h4> {
                    props?.policy === "auto-approved" && <div> Pay to Sign Up Now </div>} {
                    props?.policy === "first-come-first-served" && (
                        <div >
                            You will be only charged upon approval by organizer, but {" "}
                            <b> Approve Pay </b> to enroll once the aproval is done!
                        </div>
                    )
                } <div> Pls enter your credit card details! </div>
                <TextField
                    id="outlined-basic"
                    label="Credit Card Number"
                    variant="outlined"
                    value={creditCard}
                    onChange={e => { setcreditCard(e.target.value); setDisabled(false) }}
                />

                <Button onClick={handlePay}
                    disabled={disabled} >
                    Approve Pay </Button>
                <Button onClick={handleClose} >
                    Close </Button>
                <div >
                    Note: This doesn 't guarantee your <b>Sign Up</b> for the event!
                </div>
            </Box>
        </Modal>
    </>);
}