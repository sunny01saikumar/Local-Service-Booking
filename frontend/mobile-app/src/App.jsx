import React, { createContext, useContext, useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate, useNavigate, useParams, Link } from 'react-router-dom';
import {
  ThemeProvider, createTheme, CssBaseline, Box, Container, Paper, Typography,
  TextField, Button, Grid, Card, CardContent, Avatar, Badge, Slider, Switch,
  FormControlLabel, List, ListItem, ListItemAvatar, ListItemText, Divider,
  IconButton, Chip, Tab, Tabs, Dialog, DialogTitle, DialogContent, DialogActions,
  LinearProgress, Rating, AppBar, Toolbar, InputAdornment, BottomNavigation, BottomNavigationAction
} from '@mui/material';
import {
  Search as SearchIcon, AccountCircle, History, AccountBalanceWallet, Work,
  ArrowBack, Send, CheckCircle, LocationOn, Star, Notifications, Chat, Phone,
  CameraAlt, Share, Help, PersonAdd, LocalActivity, Info, Assessment
} from '@mui/icons-material';

// --- THEME CONFIGURATION ---
const theme = createTheme({
  palette: {
    mode: 'dark',
    primary: { main: '#6366f1' }, // Indigo
    secondary: { main: '#a855f7' }, // Purple
    background: {
      default: '#0b0f19',
      paper: '#111827'
    },
    text: {
      primary: '#f3f4f6',
      secondary: '#9ca3af'
    }
  },
  typography: {
    fontFamily: "'Inter', sans-serif",
    h1: { fontFamily: "'Outfit', sans-serif", fontWeight: 800 },
    h2: { fontFamily: "'Outfit', sans-serif", fontWeight: 700 },
    h3: { fontFamily: "'Outfit', sans-serif", fontWeight: 700 },
    h4: { fontFamily: "'Outfit', sans-serif", fontWeight: 600 },
    h5: { fontFamily: "'Outfit', sans-serif", fontWeight: 600 },
    h6: { fontFamily: "'Outfit', sans-serif", fontWeight: 600 },
    button: { textTransform: 'none', fontWeight: 600 }
  },
  shape: { borderRadius: 12 },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: '8px',
          padding: '10px 20px',
          boxShadow: 'none',
          '&:hover': { boxShadow: '0 0 15px rgba(99, 102, 241, 0.4)' }
        }
      }
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          backgroundImage: 'none',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)'
        }
      }
    }
  }
});

// --- AUTH CONTEXT & BACKEND SIMULATOR ---
const AuthContext = createContext(null);

const INITIAL_CATEGORIES = [
  { id: '1', code: 'ELECTRICIAN', name: 'Electrician', icon: '⚡', desc: 'Fan installation, wiring repair, short circuits' },
  { id: '2', code: 'PLUMBER', name: 'Plumber', icon: '🚰', desc: 'Pipe leaks, tap installations, blockages' },
  { id: '3', code: 'AC_REPAIR', name: 'AC Repair & Service', icon: '❄️', desc: 'Gas filling, filter cleaning, cooling issues' },
  { id: '4', code: 'TV_REPAIR', name: 'TV Repair', icon: '📺', desc: 'LED TV, screen panel replacement, board fixes' },
  { id: '5', code: 'LAPTOP_REPAIR', name: 'Laptop/Mobile Repair', icon: '💻', desc: 'OS installation, screen repair, batteries' },
  { id: '6', code: 'CLEANING', name: 'Deep Cleaning', icon: '🧹', desc: 'Sofa cleaning, bathroom cleaning, full house' },
  { id: '7', code: 'PEST_CONTROL', name: 'Pest Control', icon: '🐜', desc: 'Cockroach, bed bugs, termites control' },
  { id: '8', code: 'PET_CARE', name: 'Pet Care & Grooming', icon: '🐶', desc: 'Pet bath, hair cutting, veterinary check' }
];

const INITIAL_SERVICES = [
  { id: 's1', categoryId: '3', name: 'AC Complete Servicing', price: 999, discountPrice: 799, duration: 60, desc: 'Deep jet cleaning of filter and coils, drain pipe flush, gas pressure check.' },
  { id: 's2', categoryId: '3', name: 'AC Gas Charging (R32/R410)', price: 2499, discountPrice: 1999, duration: 90, desc: 'Complete leak fixing, vacuuming, and full gas charging.' },
  { id: 's3', categoryId: '1', name: 'Ceiling Fan Installation', price: 199, discountPrice: 149, duration: 30, desc: 'Hanging fan, regulator connection, and safety testing.' },
  { id: 's4', categoryId: '1', name: 'Short Circuit Repair', price: 499, discountPrice: 399, duration: 45, desc: 'Troubleshooting and fixing MCB trips or wiring damage.' },
  { id: 's5', categoryId: '2', name: 'Drainage Pipe Blockage Cleansing', price: 599, discountPrice: 449, duration: 45, desc: 'Clearing blockage in kitchen sink, bathroom, or basin lines.' },
  { id: 's6', categoryId: '6', name: 'Bathroom Deep Cleaning (Single)', price: 799, discountPrice: 599, duration: 90, desc: 'Acid wash, stain removal, grout cleaning, mirror polish.' }
];

export default function App() {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('localhub_user');
    return saved ? JSON.parse(saved) : null;
  });
  const [bookings, setBookings] = useState(() => {
    const saved = localStorage.getItem('localhub_bookings');
    return saved ? JSON.parse(saved) : [];
  });
  const [wallet, setWallet] = useState(() => {
    const saved = localStorage.getItem('localhub_wallet');
    return saved ? JSON.parse(saved) : { balance: 1500, transactions: [
      { id: 't0', type: 'CREDIT', amount: 1500, date: new Date().toISOString(), description: 'Welcome wallet bonus credited' }
    ]};
  });
  const [kycDocs, setKycDocs] = useState(() => {
    const saved = localStorage.getItem('localhub_kyc');
    return saved ? JSON.parse(saved) : [];
  });
  const [businessSettings, setBusinessSettings] = useState(() => {
    const saved = localStorage.getItem('localhub_biz_settings');
    return saved ? JSON.parse(saved) : {
      name: 'Godavarikhani Home Repairs',
      radius: 6,
      openTime: '09:00',
      closeTime: '19:00',
      isEmergency: true
    };
  });
  const [chats, setChats] = useState(() => {
    const saved = localStorage.getItem('localhub_chats');
    return saved ? JSON.parse(saved) : {};
  });

  useEffect(() => {
    localStorage.setItem('localhub_user', user ? JSON.stringify(user) : '');
  }, [user]);

  useEffect(() => {
    localStorage.setItem('localhub_bookings', JSON.stringify(bookings));
  }, [bookings]);

  useEffect(() => {
    localStorage.setItem('localhub_wallet', JSON.stringify(wallet));
  }, [wallet]);

  useEffect(() => {
    localStorage.setItem('localhub_kyc', JSON.stringify(kycDocs));
  }, [kycDocs]);

  useEffect(() => {
    localStorage.setItem('localhub_biz_settings', JSON.stringify(businessSettings));
  }, [businessSettings]);

  useEffect(() => {
    localStorage.setItem('localhub_chats', JSON.stringify(chats));
  }, [chats]);

  const loginUser = (email, role) => {
    const newUser = {
      id: UUID(),
      email,
      firstName: email.split('@')[0],
      lastName: 'User',
      phone: '+919000000022',
      role: role.toUpperCase(),
      referralCode: 'LH-' + Math.random().toString(36).substring(2, 7).toUpperCase()
    };
    setUser(newUser);
  };

  const logout = () => {
    setUser(null);
  };

  const topUpWallet = (amount) => {
    const topupAmount = parseFloat(amount);
    const newTx = {
      id: 't-' + Math.random().toString(36).substring(2, 7),
      type: 'CREDIT',
      amount: topupAmount,
      date: new Date().toISOString(),
      description: 'Topped up wallet via Razorpay Mock'
    };
    setWallet(prev => ({
      balance: prev.balance + topupAmount,
      transactions: [newTx, ...prev.transactions]
    }));
  };

  const addBooking = (bookingData) => {
    const newBooking = {
      id: 'b-' + Math.random().toString(36).substring(2, 7),
      bookingNumber: 'LH-' + Math.random().toString(36).substring(2, 8).toUpperCase(),
      status: 'CREATED',
      createdAt: new Date().toISOString(),
      ...bookingData
    };
    setBookings(prev => [newBooking, ...prev]);

    // Send WhatsApp simulator log
    console.log(`[WHATSAPP MOCK] Sent Accept/Reject booking notification to nearby providers in Godavarikhani... Booking Number: ${newBooking.bookingNumber}`);
    return newBooking;
  };

  const updateBookingStatus = (id, status) => {
    setBookings(prev => prev.map(b => {
      if (b.id === id) {
        // Handle payments on complete confirmation
        if (status === 'PAID') {
          // Debit customer wallet
          const price = b.totalAmount;
          const commission = price * 0.085; // 8.5%
          const earnings = price - commission;

          // Only adjust wallet if the user is the customer
          if (user.role === 'CUSTOMER') {
            const debTx = {
              id: 'tw-' + Math.random().toString(36).substring(2, 7),
              type: 'DEBIT',
              amount: price,
              date: new Date().toISOString(),
              description: `Paid for booking ${b.bookingNumber}`
            };
            setWallet(w => ({
              balance: w.balance - price,
              transactions: [debTx, ...w.transactions]
            }));
          }
          console.log(`[PAYMENT SPLIT] Released: provider wallet credited: INR ${earnings.toFixed(2)}, platform admin wallet commission: INR ${commission.toFixed(2)}`);
        }
        return { ...b, status };
      }
      return b;
    }));
  };

  const addChatMessage = (bookingId, text, type = 'TEXT', mediaUrl = null, lat = null, lng = null) => {
    const msg = {
      id: 'm-' + Math.random().toString(36).substring(2, 7),
      senderId: user.id,
      senderRole: user.role,
      type,
      content: text,
      mediaUrl,
      latitude: lat,
      longitude: lng,
      timestamp: new Date().toISOString(),
      read: false
    };
    setChats(prev => {
      const roomMsgs = prev[bookingId] ? [...prev[bookingId], msg] : [msg];
      return { ...prev, [bookingId]: roomMsgs };
    });
  };

  function UUID() {
    return 'u-' + Math.random().toString(36).substring(2, 10);
  }

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthContext.Provider value={{
        user, loginUser, logout, bookings, addBooking, updateBookingStatus,
        wallet, topUpWallet, kycDocs, setKycDocs, businessSettings, setBusinessSettings,
        chats, addChatMessage, categories: INITIAL_CATEGORIES, services: INITIAL_SERVICES
      }}>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<HomeSelector />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/customer/*" element={user && user.role === 'CUSTOMER' ? <CustomerApp /> : <Navigate to="/login" />} />
            <Route path="/provider/*" element={user && user.role === 'SHOP_OWNER' ? <ProviderApp /> : <Navigate to="/login" />} />
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        </BrowserRouter>
      </AuthContext.Provider>
    </ThemeProvider>
  );
}

// --- GATEWAY OR SPLASH CHOOSE PAGE ---
function HomeSelector() {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (user) {
      if (user.role === 'CUSTOMER') navigate('/customer/home');
      else if (user.role === 'SHOP_OWNER') navigate('/provider/home');
    }
  }, [user]);

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', p: 3 }}>
      <Container maxWidth="sm">
        <Paper className="glass-panel" sx={{ p: 4, textAlign: 'center', border: '1px solid rgba(99, 102, 241, 0.2)' }}>
          <Typography variant="h3" gutterBottom sx={{ fontFamily: 'Outfit', fontWeight: 800 }}>
            📍 LocalHub
          </Typography>
          <Typography variant="subtitle1" color="text.secondary" paragraph>
            Hyperlocal services marketplace connecting customers with verified providers instantly in Godavarikhani.
          </Typography>
          <Box sx={{ mt: 4, display: 'flex', flexDirection: 'column', gap: 2 }}>
            <Button variant="contained" color="primary" size="large" onClick={() => navigate('/login?role=customer')}>
              Access Customer App
            </Button>
            <Button variant="outlined" color="secondary" size="large" onClick={() => navigate('/login?role=provider')}>
              Access Provider App
            </Button>
            <Button variant="text" size="small" component="a" href="https://local-service-booking-2de9.vercel.app" target="_blank" color="warning" startIcon={<Assessment />}>
              Open Admin Panel (Cloud Dashboard)
            </Button>
          </Box>
        </Paper>
      </Container>
    </Box>
  );
}

// --- AUTHENTICATION SCREENS ---
function Login() {
  const { loginUser } = useContext(AuthContext);
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('CUSTOMER');

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const queryRole = params.get('role');
    if (queryRole === 'provider') setRole('SHOP_OWNER');
    else setRole('CUSTOMER');
  }, []);

  const handleLogin = (e) => {
    e.preventDefault();
    if (!email) return;
    loginUser(email, role);
    if (role === 'CUSTOMER') navigate('/customer/home');
    else navigate('/provider/home');
  };

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', alignItems: 'center', p: 3 }}>
      <Container maxWidth="xs">
        <Paper className="glass-panel" sx={{ p: 4 }}>
          <Typography variant="h4" align="center" gutterBottom sx={{ fontFamily: 'Outfit' }}>
            Sign In
          </Typography>
          <Typography variant="body2" color="text.secondary" align="center" gutterBottom>
            Welcome back to LocalHub
          </Typography>
          <Box component="form" onSubmit={handleLogin} sx={{ mt: 3, display: 'flex', flexDirection: 'column', gap: 2.5 }}>
            <Tabs value={role} onChange={(e, val) => setRole(val)} centered variant="fullWidth">
              <Tab label="Customer" value="CUSTOMER" />
              <Tab label="Provider" value="SHOP_OWNER" />
            </Tabs>
            <TextField label="Email Address" type="email" fullWidth variant="outlined" value={email} onChange={e => setEmail(e.target.value)} required />
            <TextField label="Password" type="password" fullWidth variant="outlined" value={password} onChange={e => setPassword(e.target.value)} required />
            <Button type="submit" variant="contained" color="primary" fullWidth size="large">
              Sign In
            </Button>
            <Typography variant="body2" align="center" color="text.secondary">
              Don't have an account? <Link to={`/register?role=${role === 'CUSTOMER' ? 'customer' : 'provider'}`} style={{ color: '#6366f1' }}>Register now</Link>
            </Typography>
          </Box>
        </Paper>
      </Container>
    </Box>
  );
}

function Register() {
  const { loginUser } = useContext(AuthContext);
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ firstName: '', email: '', phone: '', password: '' });
  const [role, setRole] = useState('CUSTOMER');

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const queryRole = params.get('role');
    if (queryRole === 'provider') setRole('SHOP_OWNER');
    else setRole('CUSTOMER');
  }, []);

  const handleRegister = (e) => {
    e.preventDefault();
    loginUser(formData.email, role);
    if (role === 'CUSTOMER') navigate('/customer/home');
    else navigate('/provider/home');
  };

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', alignItems: 'center', p: 3 }}>
      <Container maxWidth="xs">
        <Paper className="glass-panel" sx={{ p: 4 }}>
          <Typography variant="h4" align="center" gutterBottom sx={{ fontFamily: 'Outfit' }}>
            Register
          </Typography>
          <Typography variant="body2" color="text.secondary" align="center" gutterBottom>
            Create your LocalHub account
          </Typography>
          <Box component="form" onSubmit={handleRegister} sx={{ mt: 3, display: 'flex', flexDirection: 'column', gap: 2 }}>
            <Tabs value={role} onChange={(e, val) => setRole(val)} centered variant="fullWidth">
              <Tab label="Customer" value="CUSTOMER" />
              <Tab label="Provider" value="SHOP_OWNER" />
            </Tabs>
            <TextField label="First Name" fullWidth value={formData.firstName} onChange={e => setFormData({...formData, firstName: e.target.value})} required />
            <TextField label="Email Address" type="email" fullWidth value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})} required />
            <TextField label="Mobile Number" type="tel" fullWidth value={formData.phone} onChange={e => setFormData({...formData, phone: e.target.value})} required />
            <TextField label="Password" type="password" fullWidth value={formData.password} onChange={e => setFormData({...formData, password: e.target.value})} required />
            <Button type="submit" variant="contained" color="primary" fullWidth size="large">
              Register
            </Button>
            <Typography variant="body2" align="center" color="text.secondary">
              Already have an account? <Link to="/login" style={{ color: '#6366f1' }}>Sign In</Link>
            </Typography>
          </Box>
        </Paper>
      </Container>
    </Box>
  );
}

// --- CUSTOMER APP PORTAL ---
function CustomerApp() {
  const [navVal, setNavVal] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    if (navVal === 0) navigate('home');
    else if (navVal === 1) navigate('history');
    else if (navVal === 2) navigate('wallet');
    else if (navVal === 3) navigate('profile');
  }, [navVal]);

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', flexDirection: 'column', pb: 7 }}>
      <AppBar position="sticky" sx={{ background: 'rgba(11, 15, 25, 0.85)', backdropFilter: 'blur(10px)', borderBottom: '1px solid rgba(255,255,255,0.05)' }}>
        <Toolbar sx={{ justifyContent: 'space-between' }}>
          <Typography variant="h5" sx={{ fontFamily: 'Outfit', fontWeight: 700 }}>
            📍 LocalHub
          </Typography>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <Chip size="small" label="Godavarikhani" color="primary" icon={<LocationOn />} />
            <IconButton color="inherit"><Notifications /></IconButton>
          </Box>
        </Toolbar>
      </AppBar>
      <Box sx={{ flexGrow: 1, py: 2 }}>
        <Routes>
          <Route path="home" element={<CustomerHome />} />
          <Route path="booking/:serviceId" element={<CustomerBookingWizard />} />
          <Route path="tracking/:bookingId" element={<CustomerBookingTracking />} />
          <Route path="history" element={<CustomerHistory />} />
          <Route path="wallet" element={<CustomerWallet />} />
          <Route path="profile" element={<CustomerProfile />} />
        </Routes>
      </Box>
      <BottomNavigation
        value={navVal}
        onChange={(event, newValue) => setNavVal(newValue)}
        showLabels
        sx={{ position: 'fixed', bottom: 0, left: 0, right: 0, bgcolor: '#111827', borderTop: '1px solid rgba(255,255,255,0.05)' }}
      >
        <BottomNavigationAction label="Search" icon={<SearchIcon />} />
        <BottomNavigationAction label="Bookings" icon={<History />} />
        <BottomNavigationAction label="Wallet" icon={<AccountBalanceWallet />} />
        <BottomNavigationAction label="Account" icon={<AccountCircle />} />
      </BottomNavigation>
    </Box>
  );
}

function CustomerHome() {
  const { categories, services } = useContext(AuthContext);
  const [selectedCat, setSelectedCat] = useState('3'); // Default AC Repair
  const [searchQuery, setSearchQuery] = useState('');
  const navigate = useNavigate();

  const filteredServices = services.filter(s =>
    s.categoryId === selectedCat &&
    s.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <Container maxWidth="md">
      {/* Banner */}
      <Card sx={{ mb: 3, background: 'linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%)', p: 1 }}>
        <CardContent>
          <Typography variant="h4" sx={{ fontFamily: 'Outfit', fontWeight: 800 }} gutterBottom>
            Welcome Offer! Flat ₹50 Off
          </Typography>
          <Typography variant="body1" sx={{ opacity: 0.9 }}>
            Use coupon code <Chip label="WELCOME50" size="small" color="warning" /> on your first service booking.
          </Typography>
        </CardContent>
      </Card>

      {/* Search Bar */}
      <TextField
        fullWidth
        placeholder="Search for electrician, plumber, AC repair..."
        value={searchQuery}
        onChange={e => setSearchQuery(e.target.value)}
        sx={{ mb: 3 }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon color="action" />
            </InputAdornment>
          )
        }}
      />

      {/* Categories Horizontal scrolling */}
      <Typography variant="h6" gutterBottom sx={{ fontFamily: 'Outfit' }}>
        Categories
      </Typography>
      <Box sx={{ display: 'flex', gap: 1.5, overflowX: 'auto', pb: 2, mb: 3 }} className="no-scrollbar">
        {categories.map(cat => (
          <Paper
            key={cat.id}
            onClick={() => setSelectedCat(cat.id)}
            sx={{
              p: 2, textAlign: 'center', minWidth: 100, cursor: 'pointer',
              border: selectedCat === cat.id ? '2px solid #6366f1' : '1px solid rgba(255,255,255,0.05)',
              bgcolor: selectedCat === cat.id ? 'rgba(99, 102, 241, 0.15)' : 'rgba(30,41,59,0.3)'
            }}
          >
            <Typography variant="h3" sx={{ mb: 1 }}>{cat.icon}</Typography>
            <Typography variant="body2" noWrap fontWeight="bold">{cat.name}</Typography>
          </Paper>
        ))}
      </Box>

      {/* Service Offerings list */}
      <Typography variant="h6" gutterBottom sx={{ fontFamily: 'Outfit' }}>
        Recommended Services
      </Typography>
      <Grid container spacing={2}>
        {filteredServices.map(s => (
          <Grid item xs={12} key={s.id}>
            <Card className="glass-card" sx={{ p: 1 }}>
              <CardContent sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                <Box sx={{ mr: 2 }}>
                  <Typography variant="h6" gutterBottom>{s.name}</Typography>
                  <Typography variant="body2" color="text.secondary" gutterBottom>
                    {s.desc}
                  </Typography>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5, mt: 1 }}>
                    <Typography variant="h6" color="primary">₹{s.discountPrice}</Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ textDecoration: 'line-through' }}>₹{s.price}</Typography>
                    <Chip label={`${s.duration} mins`} size="small" variant="outlined" />
                  </Box>
                </Box>
                <Button variant="contained" color="primary" onClick={() => navigate(`/customer/booking/${s.id}`)}>
                  Book
                </Button>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
}

function CustomerBookingWizard() {
  const { serviceId } = useParams();
  const { services, addBooking, wallet } = useContext(AuthContext);
  const navigate = useNavigate();
  const service = services.find(s => s.id === serviceId);

  const [date, setDate] = useState('2026-07-12');
  const [time, setTime] = useState('10:30');
  const [address, setAddress] = useState('Godavarikhani Main Chowk, Landmark: Hanuman Temple');
  const [coupon, setCoupon] = useState('');
  const [couponApplied, setCouponApplied] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState('WALLET');

  if (!service) return <Typography>Service not found</Typography>;

  const finalAmount = couponApplied ? service.discountPrice - 50 : service.discountPrice;

  const handleBook = () => {
    if (paymentMethod === 'WALLET' && wallet.balance < finalAmount) {
      alert("Insufficient Wallet Balance! Please top up first.");
      return;
    }

    const booking = addBooking({
      serviceId,
      serviceName: service.name,
      scheduledStart: `${date}T${time}:00`,
      totalAmount: finalAmount,
      address,
      paymentMethod,
      providerId: '00000000-0000-0000-0000-000000005001', // assign to Anita Local Mart
      providerName: 'Anita Owner (Basic visit provider)'
    });
    navigate(`/customer/tracking/${booking.id}`);
  };

  return (
    <Container maxWidth="sm">
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 3 }}>
        <IconButton onClick={() => navigate(-1)}><ArrowBack /></IconButton>
        <Typography variant="h5" sx={{ fontFamily: 'Outfit' }}>Configure Booking</Typography>
      </Box>

      <Paper className="glass-panel" sx={{ p: 3, display: 'flex', flexDirection: 'column', gap: 3 }}>
        {/* Service summary */}
        <Box>
          <Typography variant="h6" color="primary">{service.name}</Typography>
          <Typography variant="body2" color="text.secondary">Charge: ₹{service.discountPrice}</Typography>
        </Box>

        {/* Date and Time selectors */}
        <Grid container spacing={2}>
          <Grid item xs={6}>
            <TextField label="Choose Date" type="date" fullWidth value={date} onChange={e => setDate(e.target.value)} InputLabelProps={{ shrink: true }} />
          </Grid>
          <Grid item xs={6}>
            <TextField label="Choose Time" type="time" fullWidth value={time} onChange={e => setTime(e.target.value)} InputLabelProps={{ shrink: true }} />
          </Grid>
        </Grid>

        {/* Address */}
        <TextField label="Service Address in Godavarikhani" multiline rows={2} fullWidth value={address} onChange={e => setAddress(e.target.value)} />

        {/* Map location coordinate mock */}
        <Paper variant="outlined" sx={{ p: 2, bgcolor: '#1f2937', textAlign: 'center' }}>
          <Typography variant="subtitle2" color="warning.main">📍 Hyperlocal GPS Coordinates Registered</Typography>
          <Typography variant="caption" color="text.secondary">Godavarikhani (18.8000° N, 79.4500° E)</Typography>
        </Paper>

        {/* Coupon Code input */}
        <Box sx={{ display: 'flex', gap: 1 }}>
          <TextField label="Apply Coupon" size="small" placeholder="WELCOME50" value={coupon} onChange={e => setCoupon(e.target.value)} />
          <Button variant="outlined" onClick={() => setCouponApplied(coupon.toUpperCase() === 'WELCOME50')}>
            Apply
          </Button>
        </Box>
        {couponApplied && <Chip color="success" label="Welcome coupon applied: ₹50 saved!" onDelete={() => setCouponApplied(false)} />}

        {/* Billing breakdown */}
        <Divider />
        <Box>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
            <Typography variant="body2" color="text.secondary">Subtotal</Typography>
            <Typography variant="body2">₹{service.discountPrice}</Typography>
          </Box>
          {couponApplied && (
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
              <Typography variant="body2" color="success.main">Coupon Discount</Typography>
              <Typography variant="body2" color="success.main">-₹50</Typography>
            </Box>
          )}
          <Box sx={{ display: 'flex', justifyContent: 'space-between', fontWeight: 'bold' }}>
            <Typography variant="body1">Total to Pay</Typography>
            <Typography variant="body1">₹{finalAmount}</Typography>
          </Box>
        </Box>

        {/* Payment method selection */}
        <Tabs value={paymentMethod} onChange={(e, val) => setPaymentMethod(val)} variant="fullWidth">
          <Tab label="Wallet Balance" value="WALLET" />
          <Tab label="Cash on Delivery" value="COD" />
        </Tabs>
        {paymentMethod === 'WALLET' && (
          <Typography variant="caption" color={wallet.balance >= finalAmount ? "success.main" : "error.main"}>
            Available Wallet Balance: ₹{wallet.balance}
          </Typography>
        )}

        <Button variant="contained" color="primary" fullWidth size="large" onClick={handleBook}>
          Confirm & Create Booking
        </Button>
      </Paper>
    </Container>
  );
}

function CustomerBookingTracking() {
  const { bookingId } = useParams();
  const { bookings, chats, addChatMessage, updateBookingStatus, user } = useContext(AuthContext);
  const navigate = useNavigate();
  const booking = bookings.find(b => b.id === bookingId);
  const [chatOpen, setChatOpen] = useState(false);
  const [typedMessage, setTypedMessage] = useState('');

  if (!booking) return <Typography>Booking not found</Typography>;

  const roomMessages = chats[bookingId] || [];

  const handleSend = () => {
    if (!typedMessage.trim()) return;
    addChatMessage(bookingId, typedMessage);
    setTypedMessage('');
  };

  // Simulating provider movements on status updates
  const getSimulatedStatus = () => {
    switch (booking.status) {
      case 'CREATED': return { step: 0, label: 'Waiting for Provider Accept', color: 'info' };
      case 'ACCEPTED': return { step: 1, label: 'Provider Accepted Booking', color: 'success' };
      case 'ON_THE_WAY': return { step: 2, label: 'Provider Heading to Your House', color: 'warning' };
      case 'ARRIVED': return { step: 3, label: 'Provider has Arrived', color: 'success' };
      case 'WORK_STARTED': return { step: 4, label: 'Work is in Progress', color: 'primary' };
      case 'WORK_COMPLETED': return { step: 5, label: 'Work Completed! Release Funds', color: 'success' };
      case 'PAID': return { step: 6, label: 'Completed & Paid', color: 'success' };
      default: return { step: 0, label: 'Pending', color: 'info' };
    }
  };

  const statusInfo = getSimulatedStatus();

  return (
    <Container maxWidth="sm">
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 3 }}>
        <IconButton onClick={() => navigate('/customer/history')}><ArrowBack /></IconButton>
        <Typography variant="h5" sx={{ fontFamily: 'Outfit' }}>Track Service Booking</Typography>
      </Box>

      {/* Simulated Map */}
      <Paper className="glass-panel" sx={{ p: 0, height: 280, mb: 3, overflow: 'hidden', position: 'relative', border: '1px solid rgba(99,102,241,0.2)' }}>
        {/* Background SVG representing Godavarikhani map roads */}
        <svg style={{ width: '100%', height: '100%', backgroundColor: '#0f172a' }}>
          <path d="M 0 50 Q 150 100 200 300 T 500 200" stroke="#334155" strokeWidth="6" fill="none" />
          <path d="M 100 0 Q 300 200 150 500" stroke="#334155" strokeWidth="4" fill="none" />
          {/* Customer location */}
          <circle cx="150" cy="180" r="10" fill="#ef4444" />
          <text x="170" y="185" fill="#f87171" fontSize="12" fontWeight="bold">You (Home)</text>

          {/* Provider location moving based on status */}
          {booking.status === 'ON_THE_WAY' && (
            <>
              <circle cx="210" cy="200" r="8" fill="#6366f1" />
              <text x="225" y="205" fill="#818cf8" fontSize="12" fontWeight="bold">Ramesh (On the Way)</text>
              <line x1="210" y1="200" x2="150" y2="180" stroke="#6366f1" strokeDasharray="5" strokeWidth="2" />
            </>
          )}
          {booking.status === 'ARRIVED' || booking.status === 'WORK_STARTED' || booking.status === 'WORK_COMPLETED' ? (
            <>
              <circle cx="155" cy="185" r="8" fill="#10b981" />
              <text x="175" y="205" fill="#34d399" fontSize="12" fontWeight="bold">Provider Arrived</text>
            </>
          ) : null}
        </svg>

        <Box sx={{ position: 'absolute', bottom: 15, left: 15, right: 15 }}>
          <Chip label={statusInfo.label} color={statusInfo.color} sx={{ py: 2, px: 1, fontWeight: 'bold' }} />
        </Box>
      </Paper>

      {/* Booking Details Card */}
      <Paper className="glass-panel" sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6" gutterBottom>{booking.serviceName}</Typography>
        <Typography variant="body2" color="text.secondary" paragraph>
          Booking: #{booking.bookingNumber} | Scheduled: {booking.scheduledStart.replace('T', ' ')}
        </Typography>
        <LinearProgress variant="determinate" value={(statusInfo.step / 6) * 100} color={statusInfo.color} sx={{ mb: 2, height: 8, borderRadius: 4 }} />
        
        <Box sx={{ display: 'flex', gap: 2, mt: 3 }}>
          <Button variant="contained" color="secondary" startIcon={<Chat />} fullWidth onClick={() => setChatOpen(true)}>
            Chat Provider
          </Button>
          <Button variant="outlined" color="primary" startIcon={<Phone />} fullWidth component="a" href="tel:+919000000003">
            Call
          </Button>
        </Box>

        {/* Payment execution block when complete */}
        {booking.status === 'WORK_COMPLETED' && (
          <Box sx={{ mt: 3, p: 2, bgcolor: 'rgba(16, 185, 129, 0.1)', border: '1px solid #10b981', borderRadius: '8px', textAlign: 'center' }}>
            <Typography variant="body1" fontWeight="bold" gutterBottom>Release Payment</Typography>
            <Typography variant="body2" color="text.secondary" paragraph>
              The provider completed the AC service. Confirm and release funds of ₹{booking.totalAmount}.
            </Typography>
            <Button variant="contained" color="success" onClick={() => updateBookingStatus(booking.id, 'PAID')}>
              Confirm & Release Funds
            </Button>
          </Box>
        )}
      </Paper>

      {/* Chat Drawers */}
      <Dialog open={chatOpen} onClose={() => setChatOpen(false)} fullWidth maxWidth="xs">
        <DialogTitle sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h6">Chat Room</Typography>
          <IconButton onClick={() => setChatOpen(false)}><ArrowBack /></IconButton>
        </DialogTitle>
        <DialogContent dividers sx={{ height: 350, display: 'flex', flexDirection: 'column', gap: 1.5, overflowY: 'auto' }}>
          {roomMessages.map(m => (
            <Box key={m.id} alignSelf={m.senderRole === user.role ? 'flex-end' : 'flex-start'} sx={{ maxWidth: '75%' }}>
              <Paper sx={{ p: 1.5, bgcolor: m.senderRole === user.role ? '#6366f1' : '#374151' }}>
                <Typography variant="body1">{m.content}</Typography>
                <Typography variant="caption" sx={{ display: 'block', mt: 0.5, opacity: 0.7, textAlign: 'right' }}>
                  {m.timestamp.substring(11, 16)}
                </Typography>
              </Paper>
            </Box>
          ))}
          {roomMessages.length === 0 && (
            <Typography align="center" color="text.secondary" sx={{ mt: 4 }}>No messages yet. Send a greeting!</Typography>
          )}
        </DialogContent>
        <DialogActions>
          <TextField
            fullWidth
            placeholder="Type message..."
            value={typedMessage}
            onChange={e => setTypedMessage(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && handleSend()}
            InputProps={{
              endAdornment: (
                <IconButton color="primary" onClick={handleSend}><Send /></IconButton>
              )
            }}
          />
        </DialogActions>
      </Dialog>
    </Container>
  );
}

function CustomerHistory() {
  const { bookings, updateBookingStatus } = useContext(AuthContext);
  const navigate = useNavigate();

  return (
    <Container maxWidth="md">
      <Typography variant="h4" sx={{ fontFamily: 'Outfit', mb: 3 }}>Booking History</Typography>
      <Grid container spacing={2}>
        {bookings.map(b => (
          <Grid item xs={12} key={b.id}>
            <Card className="glass-card" sx={{ p: 1 }}>
              <CardContent sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Box>
                  <Typography variant="h6">{b.serviceName}</Typography>
                  <Typography variant="body2" color="text.secondary">Order: #{b.bookingNumber}</Typography>
                  <Typography variant="body2">Amount: ₹{b.totalAmount} | Scheduled: {b.scheduledStart.replace('T', ' ')}</Typography>
                  <Box sx={{ mt: 1 }}>
                    <Chip label={b.status} color={b.status === 'PAID' ? 'success' : b.status === 'CANCELLED' ? 'error' : 'warning'} size="small" />
                  </Box>
                </Box>
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                  <Button variant="outlined" size="small" onClick={() => navigate(`/customer/tracking/${b.id}`)}>
                    Details / Track
                  </Button>
                  {b.status !== 'PAID' && b.status !== 'CANCELLED' && b.status !== 'WORK_COMPLETED' && (
                    <Button variant="text" color="error" size="small" onClick={() => updateBookingStatus(b.id, 'CANCELLED')}>
                      Cancel
                    </Button>
                  )}
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
        {bookings.length === 0 && (
          <Grid item xs={12}>
            <Typography align="center" color="text.secondary" sx={{ mt: 5 }}>No bookings recorded. Start searching for a provider!</Typography>
          </Grid>
        )}
      </Grid>
    </Container>
  );
}

function CustomerWallet() {
  const { wallet, topUpWallet } = useContext(AuthContext);
  const [topupAmount, setTopupAmount] = useState('500');

  const handleDeposit = () => {
    topUpWallet(topupAmount);
    alert(`Mock Razorpay callback successfully captured! Credited: ₹${topupAmount}`);
  };

  return (
    <Container maxWidth="sm">
      <Typography variant="h4" sx={{ fontFamily: 'Outfit', mb: 3 }}>My Wallet</Typography>
      <Paper className="glass-panel" sx={{ p: 4, textAlign: 'center', mb: 3 }}>
        <Typography variant="h5" color="text.secondary">Current Balance</Typography>
        <Typography variant="h2" color="primary" sx={{ my: 2, fontFamily: 'Outfit', fontWeight: 800 }}>
          ₹{wallet.balance}
        </Typography>

        <Divider sx={{ my: 3 }} />

        <Typography variant="h6" align="left" gutterBottom>Quick Recharge (Razorpay Simulation)</Typography>
        <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
          <TextField label="Amount (INR)" fullWidth type="number" value={topupAmount} onChange={e => setTopupAmount(e.target.value)} />
          <Button variant="contained" color="secondary" onClick={handleDeposit} startIcon={<AccountBalanceWallet />}>
            TopUp
          </Button>
        </Box>
      </Paper>

      <Typography variant="h6" gutterBottom sx={{ fontFamily: 'Outfit' }}>Transactions List</Typography>
      <List sx={{ bgcolor: 'background.paper', borderRadius: 2 }}>
        {wallet.transactions.map(t => (
          <React.Fragment key={t.id}>
            <ListItem>
              <ListItemText
                primary={t.description}
                secondary={t.date.replace('T', ' ').substring(0, 16)}
              />
              <Typography variant="h6" color={t.type === 'CREDIT' ? 'success.main' : 'error.main'}>
                {t.type === 'CREDIT' ? '+' : '-'}₹{t.amount}
              </Typography>
            </ListItem>
            <Divider />
          </React.Fragment>
        ))}
      </List>
    </Container>
  );
}

function CustomerProfile() {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  return (
    <Container maxWidth="sm">
      <Typography variant="h4" sx={{ fontFamily: 'Outfit', mb: 3 }}>My Profile</Typography>
      <Paper className="glass-panel" sx={{ p: 4, textAlign: 'center', mb: 3 }}>
        <Avatar sx={{ width: 80, height: 80, mx: 'auto', mb: 2, bgcolor: '#6366f1' }}>
          {user.firstName.substring(0, 2).toUpperCase()}
        </Avatar>
        <Typography variant="h5">{user.firstName} {user.lastName}</Typography>
        <Typography variant="body2" color="text.secondary" gutterBottom>{user.email}</Typography>
        <Typography variant="body2" color="text.secondary" paragraph>{user.phone}</Typography>

        <Divider sx={{ my: 3 }} />

        <Typography variant="h6" align="left" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <Share color="primary" /> Invite Friends (Earn ₹100 Wallet Bonus)
        </Typography>
        <Paper variant="outlined" sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center', bgcolor: '#1f2937' }}>
          <Typography variant="h6" color="warning.main" sx={{ letterSpacing: 1.5 }}>{user.referralCode}</Typography>
          <Button size="small" variant="contained" onClick={() => alert("Copied referral link: http://localhub.test/register?ref=" + user.referralCode)}>
            Copy Link
          </Button>
        </Paper>
        
        <Button variant="outlined" color="error" fullWidth sx={{ mt: 4 }} onClick={() => { logout(); navigate('/'); }}>
          Logout
        </Button>
      </Paper>
    </Container>
  );
}

// --- PROVIDER APP PORTAL ---
function ProviderApp() {
  const { kycDocs, setKycDocs, bookings, updateBookingStatus, businessSettings, setBusinessSettings } = useContext(AuthContext);
  const [tabVal, setTabVal] = useState(0);

  const pendingKyc = kycDocs.length === 0;

  const handleKycSubmit = (docData) => {
    setKycDocs([docData]);
    alert("KYC documents submitted for review! Admin can approve it from the panel.");
  };

  if (pendingKyc) {
    return <ProviderKycSubmit onSubmit={handleKycSubmit} />;
  }

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <AppBar position="sticky" sx={{ background: '#111827', borderBottom: '1px solid rgba(255,255,255,0.05)' }}>
        <Toolbar sx={{ justifyContent: 'space-between' }}>
          <Typography variant="h6" sx={{ fontFamily: 'Outfit' }}>Provider App Portal</Typography>
          <Chip label="Verified Partner" color="success" size="small" />
        </Toolbar>
      </AppBar>

      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={tabVal} onChange={(e, val) => setTabVal(val)} variant="fullWidth">
          <Tab icon={<Work />} label="My Jobs" />
          <Tab icon={<AccountBalanceWallet />} label="Earnings" />
          <Tab icon={<AccountCircle />} label="Settings" />
        </Tabs>
      </Box>

      <Box sx={{ flexGrow: 1, py: 3 }}>
        {tabVal === 0 && <ProviderJobs bookings={bookings} updateStatus={updateBookingStatus} />}
        {tabVal === 1 && <ProviderEarnings bookings={bookings} />}
        {tabVal === 2 && <ProviderSettings settings={businessSettings} setSettings={setBusinessSettings} />}
      </Box>
    </Box>
  );
}

function ProviderKycSubmit({ onSubmit }) {
  const [aadhaar, setAadhaar] = useState('');
  const [pan, setPan] = useState('');
  const [upi, setUpi] = useState('');
  const [bank, setBank] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!aadhaar || !pan) return;
    onSubmit({
      id: 'kyc-' + Math.random().toString(36).substring(2, 7),
      aadhaar,
      pan,
      upi,
      bank,
      status: 'VERIFIED' // Seed status as verified directly in simulation
    });
  };

  return (
    <Container maxWidth="xs" sx={{ mt: 5 }}>
      <Paper className="glass-panel" sx={{ p: 4 }}>
        <Typography variant="h4" align="center" gutterBottom sx={{ fontFamily: 'Outfit' }}>
          KYC Verification
        </Typography>
        <Typography variant="body2" color="text.secondary" align="center" paragraph>
          Please upload your details to activate your provider profile on LocalHub
        </Typography>
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3, display: 'flex', flexDirection: 'column', gap: 2 }}>
          <TextField label="Aadhaar Card Number" type="number" fullWidth value={aadhaar} onChange={e => setAadhaar(e.target.value)} required />
          <TextField label="PAN Card Number" fullWidth value={pan} onChange={e => setPan(e.target.value)} required />
          <TextField label="UPI Address" fullWidth value={upi} onChange={e => setUpi(e.target.value)} placeholder="name@upi" required />
          <TextField label="Bank Account Number" type="number" fullWidth value={bank} onChange={e => setBank(e.target.value)} required />
          <Button type="submit" variant="contained" color="secondary" fullWidth size="large" sx={{ mt: 2 }}>
            Submit Documents
          </Button>
        </Box>
      </Paper>
    </Container>
  );
}

function ProviderJobs({ bookings, updateStatus }) {
  const [chatOpen, setChatOpen] = useState(false);
  const [activeBookingId, setActiveBookingId] = useState(null);
  const { chats, addChatMessage, user } = useContext(AuthContext);
  const [typedMessage, setTypedMessage] = useState('');

  const providerBookings = bookings.filter(b => b.status !== 'CANCELLED');

  const getNextAction = (status) => {
    switch (status) {
      case 'CREATED': return 'Accept Booking';
      case 'ACCEPTED': return 'Start Navigation';
      case 'ON_THE_WAY': return 'Mark Arrived';
      case 'ARRIVED': return 'Start Work';
      case 'WORK_STARTED': return 'Mark Work Completed';
      case 'WORK_COMPLETED': return 'Awaiting Customer Release';
      case 'PAID': return 'Completed & Paid';
      default: return null;
    }
  };

  const getNextStatus = (status) => {
    switch (status) {
      case 'CREATED': return 'ACCEPTED';
      case 'ACCEPTED': return 'ON_THE_WAY';
      case 'ON_THE_WAY': return 'ARRIVED';
      case 'ARRIVED': return 'WORK_STARTED';
      case 'WORK_STARTED': return 'WORK_COMPLETED';
      default: return null;
    }
  };

  const handleAction = (id, status) => {
    const next = getNextStatus(status);
    if (next) updateStatus(id, next);
  };

  const roomMessages = chats[activeBookingId] || [];

  return (
    <Container maxWidth="md">
      <Typography variant="h5" sx={{ fontFamily: 'Outfit', mb: 3 }}>Active Bookings Queue</Typography>
      <Grid container spacing={2}>
        {providerBookings.map(b => (
          <Grid item xs={12} key={b.id}>
            <Card className="glass-card" sx={{ p: 1 }}>
              <CardContent sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Box>
                  <Typography variant="h6">{b.serviceName}</Typography>
                  <Typography variant="body2" color="text.secondary">Booking ID: #{b.bookingNumber}</Typography>
                  <Typography variant="body2" color="text.secondary">Address: {b.address}</Typography>
                  <Typography variant="body2" color="primary" sx={{ mt: 0.5 }}>Amount: ₹{b.totalAmount}</Typography>
                  <Chip size="small" label={b.status} color="warning" sx={{ mt: 1 }} />
                </Box>
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                  {getNextAction(b.status) && (
                    <Button
                      variant="contained"
                      color="primary"
                      disabled={b.status === 'WORK_COMPLETED'}
                      onClick={() => handleAction(b.id, b.status)}
                    >
                      {getNextAction(b.status)}
                    </Button>
                  )}
                  {b.status !== 'CREATED' && b.status !== 'PAID' && (
                    <Button
                      variant="outlined"
                      size="small"
                      color="secondary"
                      startIcon={<Chat />}
                      onClick={() => { setActiveBookingId(b.id); setChatOpen(true); }}
                    >
                      Chat
                    </Button>
                  )}
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
        {providerBookings.length === 0 && (
          <Grid item xs={12}>
            <Typography align="center" color="text.secondary" sx={{ mt: 5 }}>No bookings assigned yet. Waiting for customer requests...</Typography>
          </Grid>
        )}
      </Grid>

      {/* Provider Chat Dialog */}
      <Dialog open={chatOpen} onClose={() => setChatOpen(false)} fullWidth maxWidth="xs">
        <DialogTitle sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h6">Chat with Customer</Typography>
          <IconButton onClick={() => setChatOpen(false)}><ArrowBack /></IconButton>
        </DialogTitle>
        <DialogContent dividers sx={{ height: 350, display: 'flex', flexDirection: 'column', gap: 1.5, overflowY: 'auto' }}>
          {roomMessages.map(m => (
            <Box key={m.id} alignSelf={m.senderRole === user.role ? 'flex-end' : 'flex-start'} sx={{ maxWidth: '75%' }}>
              <Paper sx={{ p: 1.5, bgcolor: m.senderRole === user.role ? '#a855f7' : '#374151' }}>
                <Typography variant="body1">{m.content}</Typography>
                <Typography variant="caption" sx={{ display: 'block', mt: 0.5, opacity: 0.7, textAlign: 'right' }}>
                  {m.timestamp.substring(11, 16)}
                </Typography>
              </Paper>
            </Box>
          ))}
        </DialogContent>
        <DialogActions>
          <TextField
            fullWidth
            placeholder="Type message..."
            value={typedMessage}
            onChange={e => setTypedMessage(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && (addChatMessage(activeBookingId, typedMessage), setTypedMessage(''))}
            InputProps={{
              endAdornment: (
                <IconButton color="primary" onClick={() => { addChatMessage(activeBookingId, typedMessage); setTypedMessage(''); }}><Send /></IconButton>
              )
            }}
          />
        </DialogActions>
      </Dialog>
    </Container>
  );
}

function ProviderEarnings({ bookings }) {
  const completed = bookings.filter(b => b.status === 'PAID');
  const totalRevenue = completed.reduce((sum, b) => sum + b.totalAmount, 0);
  const earningsAmount = totalRevenue * 0.915; // 91.5% after 8.5% platform commission

  return (
    <Container maxWidth="sm">
      <Typography variant="h5" sx={{ fontFamily: 'Outfit', mb: 3 }}>Earnings Dashboard</Typography>
      <Paper className="glass-panel" sx={{ p: 4, textAlign: 'center', mb: 3 }}>
        <Typography variant="h6" color="text.secondary">Net Provider Earnings</Typography>
        <Typography variant="h2" color="secondary" sx={{ my: 2, fontFamily: 'Outfit', fontWeight: 800 }}>
          ₹{earningsAmount.toFixed(2)}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Platform commission deducted: ₹{(totalRevenue * 0.085).toFixed(2)} (8.5%)
        </Typography>
      </Paper>

      <Typography variant="h6" gutterBottom sx={{ fontFamily: 'Outfit' }}>Completed Bookings History</Typography>
      <List sx={{ bgcolor: 'background.paper', borderRadius: 2 }}>
        {completed.map(b => (
          <React.Fragment key={b.id}>
            <ListItem>
              <ListItemText
                primary={b.serviceName}
                secondary={`Order: #${b.bookingNumber} | Released`}
              />
              <Typography variant="h6" color="success.main">
                +₹{(b.totalAmount * 0.915).toFixed(2)}
              </Typography>
            </ListItem>
            <Divider />
          </React.Fragment>
        ))}
      </List>
    </Container>
  );
}

function ProviderSettings({ settings, setSettings }) {
  const { logout } = useContext(AuthContext);
  const navigate = useNavigate();

  return (
    <Container maxWidth="sm">
      <Typography variant="h5" sx={{ fontFamily: 'Outfit', mb: 3 }}>Service Configurations</Typography>
      <Paper className="glass-panel" sx={{ p: 4, display: 'flex', flexDirection: 'column', gap: 3 }}>
        <TextField label="Business / Shop Name" fullWidth value={settings.name} onChange={e => setSettings({...settings, name: e.target.value})} />
        
        <Box>
          <Typography gutterBottom>Service radius: {settings.radius} km</Typography>
          <Slider value={settings.radius} min={1} max={30} onChange={(e, val) => setSettings({...settings, radius: val})} valueLabelDisplay="auto" />
        </Box>

        <Grid container spacing={2}>
          <Grid item xs={6}>
            <TextField label="Opening Time" type="time" fullWidth value={settings.openTime} onChange={e => setSettings({...settings, openTime: e.target.value})} InputLabelProps={{ shrink: true }} />
          </Grid>
          <Grid item xs={6}>
            <TextField label="Closing Time" type="time" fullWidth value={settings.closeTime} onChange={e => setSettings({...settings, closeTime: e.target.value})} InputLabelProps={{ shrink: true }} />
          </Grid>
        </Grid>

        <FormControlLabel
          control={<Switch checked={settings.isEmergency} onChange={e => setSettings({...settings, isEmergency: e.target.checked})} />}
          label="Emergency Service Availability (24/7)"
        />

        <Button variant="outlined" color="error" fullWidth sx={{ mt: 3 }} onClick={() => { logout(); navigate('/'); }}>
          Logout
        </Button>
      </Paper>
    </Container>
  );
}
