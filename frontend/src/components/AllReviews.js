import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import ListItemText from '@mui/material/ListItemText';
import ListItem from '@mui/material/ListItem';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import CloseIcon from '@mui/icons-material/Close';
import Slide from '@mui/material/Slide';
import axios from "../common/axiosInstance";
import Rating from '@mui/material/Rating';
import Alert from '@mui/material/Alert';
const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});

export const AllReviews = (props) => {
    const [open, setOpen] = React.useState(props.open || false);
    const [review, setReview] = React.useState(null)
    const [forOrg, setForOrg] = React.useState(props?.forOrg || false)

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    const handleShowReviews = (e) => {
        e.preventDefault()
        let api = null;
        if (forOrg) api = 'getMyReviewsAsOrganizer';
        else api = 'getParticipantRepAndReviews';
        console.log(props)
        axios
            .get(`/${api}?id=${props?.id}`)
            .then((res) => {
                console.log(res.data)
                if (res.status == 200) {
                    setReview(res.data)
                    handleClickOpen()
                    //   // if (res.data.participantList.length > 0) setAttendees(res.data)
                }
            })
            .catch((err) => {
                console.log("in catch", err);
                //   showPopUp(err.response.data);
            })
    }

    return (
        <div style={{ gridColumn: '2' }}>
            <Button variant="contained" size="small" onClick={e => handleShowReviews(e)}>
                Show Reviews
            </Button>
            <Dialog
                fullScreen
                open={open}
                onClose={handleClose}
                TransitionComponent={Transition}
            >
                <AppBar sx={{ position: 'relative' }}>
                    <Toolbar>
                        <IconButton
                            edge="start"
                            color="inherit"
                            onClick={handleClose}
                            aria-label="close"
                        >
                            <CloseIcon />
                        </IconButton>
                        <Typography sx={{ ml: 2, flex: 1 }} variant="h6" component="div">
                            Reviews
                        </Typography>

                    </Toolbar>
                </AppBar>
                <List>
                    {
                        review?.avgReputation && review?.avgReputation > 0 && (
                            <>
                            <ListItem >
                                <ListItemText
                                    primary={
                                        <div style={{ display: 'flex', alignItems: 'center' }}>
                                            <Typography
                                                sx={{ display: 'inline', ml: 2, mr: 2 }}
                                                component="span"
                                                variant="h6"
                                                color="text.primary"
                                            >
                                                Avg. Rating
                                            </Typography>
                                            <Rating
                                                name="simple-controlled"
                                                value={review?.avgReputation}
                                                precision={0.1}
                                                sx={{
                                                    'span': { ml: 0 },
                                                    marginLeft: 0,
                                                    'pointer-events': 'none',

                                                }} />
                                        </div>
                                    }>
                                </ListItemText>
                            </ListItem>
                            <Divider />
                            <Divider />
                             </>
                        )
                    }
                    {
                        review?.reviews && review?.reviews.length > 0 && review?.reviews.map((data, i) => (
                            <React.Fragment>
                                <ListItem >
                                    <ListItemText
                                        primary={
                                            <div style={{ display: 'flex', alignItems: 'center' }}>
                                                <Rating
                                                    name="simple-controlled"
                                                    value={data?.reviewStar}
                                                    precision={0.1}
                                                    sx={{
                                                        'span': { ml: 0 },
                                                        marginLeft: 0,
                                                        'pointer-events': 'none',

                                                    }} />
                                                <Typography
                                                    sx={{ display: 'inline', ml: 2, mr: 2 }}
                                                    component="span"
                                                    variant="h6"
                                                    color="text.primary"
                                                >
                                                    {data?.reviewerName}
                                                </Typography>
                                                {'|'}
                                                <Typography
                                                    sx={{ display: 'inline', ml: 2 }}
                                                    component="span"
                                                    variant="subtitle2"
                                                    color="text.primary"
                                                >
                                                    {data?.eventName}
                                                </Typography>
                                            </div>
                                        }
                                        secondary={
                                            <React.Fragment>
                                                <Typography
                                                    sx={{ display: 'inline' }}
                                                    component="span"
                                                    variant="body2"
                                                    color="text.primary"
                                                >
                                                    {data?.reviewText}
                                                </Typography>
                                            </React.Fragment>
                                        }
                                    />
                                </ListItem>
                                <Divider />
                            </React.Fragment>
                        ))
                    }

                    {
                        !review?.reviews &&
                        (
                            <Alert severity="warning">Sorry! No Reviews</Alert>
                        )
                    }
                </List>
            </Dialog>
        </div >
    );
}