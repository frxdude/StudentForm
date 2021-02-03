import React, {useState, useEffect} from 'react';
import TextField from '@material-ui/core/TextField';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import Button from '@material-ui/core/Button';
import { withStyles, styled } from '@material-ui/core/styles';
import InputBase from '@material-ui/core/InputBase';
import { DataGrid } from '@material-ui/data-grid';
import { ToastContainer, toast } from "react-toastify";
import axios from "axios";
import './App.css';
//styles
const bodyContainer = {
  display: "flex",
  marginLeft: "97px",
  marginRight: "95px",
  marginTop: "70px"
}
const formContainer = {
  flexDirection: "column", 
  width: "200px", 
}
const headerContainer = {
  height:"100px", 
  backgroundColor:"#557b83"
}
const textSty = {
  borderRadius: "4px",
  fontFamily: 'Gotham SSm A',
  fontSize: "55px",
  fontWeight: "400",
  lineHeight: "normal",
  backgroundColor: "transparent",
  color: "#282828",
  outline: "none",
  textAlign: "center",
  paddingTop: "10px"
  // box-shadow: 0px 4px 20px 0px transparent;
}
const buttonSty = {
  height: "50px",
  width: "100px",
  alignSelf: "center",
  justifyContent: "center",
  marginLeft: "50px",
  marginRight: "50px"
}
const circleRed = {
    height: "50px",
    width: "100px",
    paddingTop: "18px",
    backgroundColor: "red",
    borderRadius: 200,
    textAlign: 'center',
    color: '#ffffff',
    position: "absolute",
    right: "100px",
    top: "60px"
}
const circleGreen = {
    height: "50px",
    width: "100px",
    paddingTop: "18px",
    backgroundColor: "green",
    borderRadius: 200,
    textAlign: 'center',
    color: '#ffffff',
    position: "absolute",
    right: "100px",
    top: "60px"
}
//end styles

const App = () => {
  const [state, setState] = React.useState({
    major: "",
    year: "",
    name: "",
    code: "",
    lab: ""
  })
  const columns = [
    { field: 'code', headerName: 'Оюутны код', width: 170 },
    { field: 'name', headerName: 'Нэр', width: 170 },
    { field: 'major', headerName: 'Мэргэжил', width: 170 },
    { field: 'year', headerName: 'Курс', width: 170 },
    { field: 'lab', headerName: 'Лаб', width: 170 }
  ];
  const [status, setStatus] = useState(true);
  const [studentList, setStudentList] = useState([]);

  const handleChange = (event) => {
    const value = event.target.value;
    setState({
      ...state,
      [event.target.name]: value
    });
  };
  
  const sendData = () => {
        axios
        .post("/student", state)
        .then((response) => {
            console.log(response.data)
            // notifySuccess()
            setStudentList(response.data)
        })
        .catch((error) => {
            // notifyFail()
            console.log(error);
        });
  }
  useEffect(() => {
    const interval = setInterval(() => {
      axios
      .get("/checkConnection", {})
      .then((response) => {
          console.log(response);
          notifyErr("WHY WE");
          if(response.status === 200 && response.data === true)
          {
            // if(status === false)
              // successNotify();
            setStatus(true);
          }
          else
          {
            // if(status === true)
              // successNotify();
            setStatus(false);
          }
      })
      .catch((error) => {
          // notifyFail()
          console.log(error);
      });
    }, 5000)
    return () => clearInterval(interval);
  }, [])

  useEffect(() => {
    console.log("Student list comes");
      axios
        .get("/student", {})
        .then((response) => {
            if(response.status === 200)
              setStudentList(response.data);
        })
        .catch((error) => {
            // notifyFail()
            console.log(error);
        });
  },[])

  const notify = (text) => {
    toast.warn(text, {
      position: "top-right",
      autoClose: 3000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
  };
  const notifyErr = (text) => {
    toast.error(text, {
      position: "top-right",
      autoClose: 3000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
  };
  return (
    <div>
      <div style={headerContainer}>
        <div style={textSty}>
          B1809100040 И. Сайнжаргал
        </div>
        <div style={status ? circleGreen : circleRed}>
            <footer>Database Status</footer>
        </div>
      </div>
      <div style={bodyContainer}>
        <div style={formContainer}>
          <MyTextField label="Лаб" name="lab" variant="outlined" value={state.lab} onChange={handleChange}/>
          <MyTextField label="Оюутны нэр" name="name" variant="outlined" value={state.name} onChange={handleChange}/>
          <MyTextField label="Оюутны код" name="code" variant="outlined" value={state.code} onChange={handleChange}/>
          <MyTextField label="Анги" name="major" variant="outlined" value={state.major} onChange={handleChange}/> 
          <FormControl>
            <InputLabel>Курс</InputLabel>
            <Select
              id="Select"
              name={"year"}
              value={state.year}
              onChange={handleChange}
              input={<BootstrapInput />}
            >
              <MenuItem value={"1"}>1</MenuItem>
              <MenuItem value={"2"}>2</MenuItem>
              <MenuItem value={"3"}>3</MenuItem>
              <MenuItem value={"4+"}>4+</MenuItem>
            </Select>
          </FormControl>
        </div>
        <Button variant="contained" color="primary" onClick={sendData} style={buttonSty}>
          Send
        </Button>
        <div style={{ height: 400, width: '100%', alignSelf:"center" }}>
          <DataGrid rows={studentList} columns={columns} />
        </div>
      </div>
    </div>
  );
}

const MyTextField = styled(TextField)({
  marginTop: "50px"
})

const BootstrapInput = withStyles((theme) => ({
  root: {
    'label + &': {
      marginTop: theme.spacing(3),
    },
  },
  input: {
    borderRadius: 4,
    position: 'relative',
    backgroundColor: theme.palette.background.paper,
    border: '1px solid #ced4da',
    fontSize: 16,
    padding: '10px 26px 10px 12px',
    transition: theme.transitions.create(['border-color', 'box-shadow']),
    '&:focus': {
      borderRadius: 4,
      borderColor: '#80bdff',
      boxShadow: '0 0 0 0.2rem rgba(0,123,255,.25)',
    },
  },
}))(InputBase);

export default App;