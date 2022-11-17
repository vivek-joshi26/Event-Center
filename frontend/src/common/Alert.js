import * as React from 'react';
import Button from '@mui/material/Button';
import Snackbar from '@mui/material/Snackbar';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';

export default class AlertModal extends React.Component { 
    constructor(props) {
        super(props)
        this.state = {
            open: this.props.open,
            msg: this.props.msg,
            modal: this.props.modal
        }
        console.log(props)
    }

handleClick ()  {
  console.log(this.state.open)
  };

handleClose = (event, reason) => {
    if (reason === 'clickaway') {
      return;
    }
    this.state.modal()
    this.setState({ open:false });
  };

action = (
    <React.Fragment>
      <Button color="secondary" size="small" onClick={this.handleClose}>
      </Button>
      <IconButton
        size="small"
        aria-label="close"
        color="inherit"
        onClick={this.handleClose}
      >
        <CloseIcon fontSize="small" />
      </IconButton>
    </React.Fragment>
  );
render(){
  return (
    <div>
      <Snackbar
        open={this.state.open}
        autoHideDuration={6000}
        onClose={this.handleClose}
        message={this.state.msg}
        action={this.action}
      />
    </div>
  );
}
}
