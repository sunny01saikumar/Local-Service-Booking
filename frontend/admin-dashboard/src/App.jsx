import React, { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Link, useNavigate, useLocation } from 'react-router-dom';
import {
  ThemeProvider, createTheme, CssBaseline, Box, Drawer, AppBar, Toolbar,
  Typography, List, ListItem, ListItemButton, ListItemIcon, ListItemText,
  Divider, Container, Grid, Card, CardContent, Button, Table, TableBody,
  TableCell, TableContainer, TableHead, TableRow, Paper, Chip, Avatar,
  TextField, Dialog, DialogTitle, DialogContent, DialogActions, IconButton
} from '@mui/material';
import {
  Dashboard as DashboardIcon, People, Work, Category, LocalOffer,
  Image, Assessment, Settings, CheckCircle, Cancel, LockOpen, Menu,
  Visibility, Refresh, HelpOutline, Notifications
} from '@mui/icons-material';

const theme = createTheme({
  palette: {
    mode: 'dark',
    primary: { main: '#6366f1' },
    secondary: { main: '#f59e0b' }, // Amber
    background: {
      default: '#0b0f19',
      paper: '#111827'
    },
    success: { main: '#10b981' }
  },
  typography: {
    fontFamily: "'Inter', sans-serif",
    h4: { fontFamily: "'Outfit', sans-serif", fontWeight: 800 },
    h5: { fontFamily: "'Outfit', sans-serif", fontWeight: 700 },
    h6: { fontFamily: "'Outfit', sans-serif", fontWeight: 600 }
  },
  shape: { borderRadius: 12 }
});

const drawerWidth = 260;

export default function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <BrowserRouter>
        <Layout />
      </BrowserRouter>
    </ThemeProvider>
  );
}

function Layout() {
  const location = useLocation();
  const navigate = useNavigate();

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh' }}>
      {/* Sidebar Navigation */}
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box', bgcolor: '#111827', borderRight: '1px solid rgba(255,255,255,0.05)' }
        }}
      >
        <Toolbar sx={{ py: 2 }}>
          <Typography variant="h5" sx={{ fontFamily: 'Outfit', fontWeight: 800, color: '#6366f1' }}>
            🛡️ LocalHub Admin
          </Typography>
        </Toolbar>
        <Divider sx={{ opacity: 0.1 }} />
        <List sx={{ px: 2, mt: 2 }}>
          <ListItem disablePadding sx={{ mb: 1 }}>
            <ListItemButton
              component={Link} to="/"
              selected={location.pathname === '/'}
              sx={{ borderRadius: 2, '&.Mui-selected': { bgcolor: 'rgba(99, 102, 241, 0.15)', color: '#6366f1' } }}
            >
              <ListItemIcon><DashboardIcon color={location.pathname === '/' ? 'primary' : 'inherit'} /></ListItemIcon>
              <ListItemText primary="Dashboard" />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding sx={{ mb: 1 }}>
            <ListItemButton
              component={Link} to="/kyc"
              selected={location.pathname === '/kyc'}
              sx={{ borderRadius: 2, '&.Mui-selected': { bgcolor: 'rgba(99, 102, 241, 0.15)', color: '#6366f1' } }}
            >
              <ListItemIcon><CheckCircle color={location.pathname === '/kyc' ? 'primary' : 'inherit'} /></ListItemIcon>
              <ListItemText primary="KYC Verification" />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding sx={{ mb: 1 }}>
            <ListItemButton
              component={Link} to="/bookings"
              selected={location.pathname === '/bookings'}
              sx={{ borderRadius: 2, '&.Mui-selected': { bgcolor: 'rgba(99, 102, 241, 0.15)', color: '#6366f1' } }}
            >
              <ListItemIcon><Work color={location.pathname === '/bookings' ? 'primary' : 'inherit'} /></ListItemIcon>
              <ListItemText primary="Bookings Supervisor" />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding sx={{ mb: 1 }}>
            <ListItemButton
              component={Link} to="/categories"
              selected={location.pathname === '/categories'}
              sx={{ borderRadius: 2, '&.Mui-selected': { bgcolor: 'rgba(99, 102, 241, 0.15)', color: '#6366f1' } }}
            >
              <ListItemIcon><Category color={location.pathname === '/categories' ? 'primary' : 'inherit'} /></ListItemIcon>
              <ListItemText primary="Categories" />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding sx={{ mb: 1 }}>
            <ListItemButton
              component={Link} to="/coupons"
              selected={location.pathname === '/coupons'}
              sx={{ borderRadius: 2, '&.Mui-selected': { bgcolor: 'rgba(99, 102, 241, 0.15)', color: '#6366f1' } }}
            >
              <ListItemIcon><LocalOffer color={location.pathname === '/coupons' ? 'primary' : 'inherit'} /></ListItemIcon>
              <ListItemText primary="Coupons" />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding sx={{ mb: 1 }}>
            <ListItemButton
              component={Link} to="/banners"
              selected={location.pathname === '/banners'}
              sx={{ borderRadius: 2, '&.Mui-selected': { bgcolor: 'rgba(99, 102, 241, 0.15)', color: '#6366f1' } }}
            >
              <ListItemIcon><Image color={location.pathname === '/banners' ? 'primary' : 'inherit'} /></ListItemIcon>
              <ListItemText primary="Marketing Banners" />
            </ListItemButton>
          </ListItem>
        </List>
      </Drawer>

      {/* Main Content Area */}
      <Box component="main" sx={{ flexGrow: 1, p: 3, bgcolor: '#0b0f19' }}>
        <AppBar position="sticky" sx={{ background: 'transparent', boxShadow: 'none', borderBottom: '1px solid rgba(255,255,255,0.05)', mb: 4 }}>
          <Toolbar sx={{ justifyContent: 'space-between' }}>
            <Typography variant="h6" color="text.secondary">
              Overview Control Panel
            </Typography>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
              <Chip label="Admin Portal Active" color="success" size="small" variant="outlined" />
              <IconButton><Notifications /></IconButton>
            </Box>
          </Toolbar>
        </AppBar>

        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/kyc" element={<KycApprovals />} />
          <Route path="/bookings" element={<BookingsList />} />
          <Route path="/categories" element={<CategoriesList />} />
          <Route path="/coupons" element={<CouponsManager />} />
          <Route path="/banners" element={<BannersManager />} />
        </Routes>
      </Box>
    </Box>
  );
}

// --- SUB PAGES ---
function Dashboard() {
  const [stats, setStats] = useState({
    revenue: 4799,
    bookings: 6,
    cancellationRate: 16.6,
    users: 24,
    activeProviders: 5
  });

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mb: 3 }}>Dashboard Analytics</Typography>

      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card className="glass-card">
            <CardContent>
              <Typography color="text.secondary" gutterBottom>Total Revenue Released</Typography>
              <Typography variant="h3" color="primary" sx={{ fontFamily: 'Outfit', fontWeight: 800 }}>
                ₹{stats.revenue}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card className="glass-card">
            <CardContent>
              <Typography color="text.secondary" gutterBottom>Total Booking Requests</Typography>
              <Typography variant="h3" sx={{ fontFamily: 'Outfit', fontWeight: 800 }}>
                {stats.bookings}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card className="glass-card">
            <CardContent>
              <Typography color="text.secondary" gutterBottom>Booking Cancellation Rate</Typography>
              <Typography variant="h3" color="error.main" sx={{ fontFamily: 'Outfit', fontWeight: 800 }}>
                {stats.cancellationRate}%
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card className="glass-card">
            <CardContent>
              <Typography color="text.secondary" gutterBottom>Registered Users Count</Typography>
              <Typography variant="h3" color="success.main" sx={{ fontFamily: 'Outfit', fontWeight: 800 }}>
                {stats.users}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Paper className="glass-panel" sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>Top Performed Services</Typography>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Service</TableCell>
                    <TableCell>Frequency</TableCell>
                    <TableCell>Earnings</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  <TableRow>
                    <TableCell>AC Complete Servicing</TableCell>
                    <TableCell>4 bookings</TableCell>
                    <TableCell>₹3196</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Ceiling Fan Installation</TableCell>
                    <TableCell>2 bookings</TableCell>
                    <TableCell>₹298</TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </Grid>
        <Grid item xs={12} md={6}>
          <Paper className="glass-panel" sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>Top Service Providers</Typography>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Provider</TableCell>
                    <TableCell>Rating</TableCell>
                    <TableCell>Status</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  <TableRow>
                    <TableCell>Anita Owner (Local Mart)</TableCell>
                    <TableCell><Chip label="4.8 ★" size="small" color="primary" /></TableCell>
                    <TableCell><Chip label="Active" color="success" size="small" /></TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Ramesh AC Services</TableCell>
                    <TableCell><Chip label="4.9 ★" size="small" color="primary" /></TableCell>
                    <TableCell><Chip label="Active" color="success" size="small" /></TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
}

function KycApprovals() {
  const [providers, setProviders] = useState([
    { id: '1', name: 'Ramesh AC Services', owner: 'Ramesh', phone: '+919876543211', docType: 'Aadhaar + PAN', docNum: '4560-1234-9876', status: 'PENDING' }
  ]);

  const handleVerify = (id, approve) => {
    setProviders(prev => prev.map(p => {
      if (p.id === id) return { ...p, status: approve ? 'APPROVED' : 'REJECTED' };
      return p;
    }));
    alert(`Provider KYC status updated to: ${approve ? 'APPROVED' : 'REJECTED'}`);
  };

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mb: 3 }}>KYC Approvals Queue</Typography>
      <TableContainer component={Paper} className="glass-panel">
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Business Name</TableCell>
              <TableCell>Owner</TableCell>
              <TableCell>Contact</TableCell>
              <TableCell>Document Type</TableCell>
              <TableCell>Doc Number</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {providers.map(p => (
              <TableRow key={p.id}>
                <TableCell fontWeight="bold">{p.name}</TableCell>
                <TableCell>{p.owner}</TableCell>
                <TableCell>{p.phone}</TableCell>
                <TableCell>{p.docType}</TableCell>
                <TableCell>{p.docNum}</TableCell>
                <TableCell>
                  <Chip label={p.status} color={p.status === 'PENDING' ? 'warning' : p.status === 'APPROVED' ? 'success' : 'error'} size="small" />
                </TableCell>
                <TableCell>
                  {p.status === 'PENDING' && (
                    <Box sx={{ display: 'flex', gap: 1 }}>
                      <Button variant="contained" color="success" size="small" onClick={() => handleVerify(p.id, true)}>Approve</Button>
                      <Button variant="outlined" color="error" size="small" onClick={() => handleVerify(p.id, false)}>Reject</Button>
                    </Box>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
}

function BookingsList() {
  const [bookings, setBookings] = useState([
    { id: 'b1', number: 'LH-F492AC', customer: 'Sai Kumar', service: 'AC Gas Charging', date: '2026-07-12', amount: '₹1999', status: 'ACCEPTED' },
    { id: 'b2', number: 'LH-A0034B', customer: 'Deepak', service: 'Ceiling Fan Installation', date: '2026-07-11', amount: '₹149', status: 'PAID' }
  ]);

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mb: 3 }}>Bookings Supervisor</Typography>
      <TableContainer component={Paper} className="glass-panel">
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Booking Code</TableCell>
              <TableCell>Customer</TableCell>
              <TableCell>Service</TableCell>
              <TableCell>Date</TableCell>
              <TableCell>Amount</TableCell>
              <TableCell>Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {bookings.map(b => (
              <TableRow key={b.id}>
                <TableCell fontWeight="bold">{b.number}</TableCell>
                <TableCell>{b.customer}</TableCell>
                <TableCell>{b.service}</TableCell>
                <TableCell>{b.date}</TableCell>
                <TableCell>{b.amount}</TableCell>
                <TableCell>
                  <Chip label={b.status} color={b.status === 'PAID' ? 'success' : 'warning'} size="small" />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
}

function CategoriesList() {
  const [categories, setCategories] = useState([
    { id: '1', code: 'ELECTRICIAN', name: 'Electrician', icon: '⚡', serviceCount: 12 },
    { id: '2', code: 'PLUMBER', name: 'Plumber', icon: '🚰', serviceCount: 9 },
    { id: '3', code: 'AC_REPAIR', name: 'AC Repair', icon: '❄️', serviceCount: 6 }
  ]);

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mb: 3 }}>Service Categories Configurator</Typography>
      <TableContainer component={Paper} className="glass-panel">
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Icon</TableCell>
              <TableCell>Code</TableCell>
              <TableCell>Category Name</TableCell>
              <TableCell>Active Services</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {categories.map(c => (
              <TableRow key={c.id}>
                <TableCell fontSize="24">{c.icon}</TableCell>
                <TableCell>{c.code}</TableCell>
                <TableCell fontWeight="bold">{c.name}</TableCell>
                <TableCell>{c.serviceCount} services</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
}

function CouponsManager() {
  const [coupons, setCoupons] = useState([
    { id: '1', code: 'WELCOME50', name: 'Welcome Bonus', discount: '₹50 flat', validity: '31 Dec 2026', status: 'ACTIVE' }
  ]);

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mb: 3 }}>Coupons & Promotions</Typography>
      <TableContainer component={Paper} className="glass-panel">
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Promo Code</TableCell>
              <TableCell>Offer Name</TableCell>
              <TableCell>Discount Value</TableCell>
              <TableCell>Validity</TableCell>
              <TableCell>Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {coupons.map(c => (
              <TableRow key={c.id}>
                <TableCell fontWeight="bold"><Chip label={c.code} color="warning" /></TableCell>
                <TableCell>{c.name}</TableCell>
                <TableCell>{c.discount}</TableCell>
                <TableCell>{c.validity}</TableCell>
                <TableCell><Chip label={c.status} color="success" size="small" /></TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
}

function BannersManager() {
  const [banners, setBanners] = useState([
    { id: '1', title: 'LocalHub Grand Launch', subtitle: 'Godavarikhani Local Services', placement: 'Home Top Banner', status: 'ACTIVE' }
  ]);

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mb: 3 }}>Marketing Banner Management</Typography>
      <TableContainer component={Paper} className="glass-panel">
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Banner Title</TableCell>
              <TableCell>Subtitle</TableCell>
              <TableCell>Placement</TableCell>
              <TableCell>Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {banners.map(b => (
              <TableRow key={b.id}>
                <TableCell fontWeight="bold">{b.title}</TableCell>
                <TableCell>{b.subtitle}</TableCell>
                <TableCell>{b.placement}</TableCell>
                <TableCell><Chip label={b.status} color="success" size="small" /></TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
}
