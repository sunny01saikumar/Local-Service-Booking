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
  const [categories, setCategories] = useState([]);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [formData, setFormData] = useState({ name: '', code: '', description: '', displayOrder: 0 });
  const [loading, setLoading] = useState(false);

  const getCategoryIcon = (code) => {
    switch (code) {
      case 'ELECTRICIAN': return '⚡';
      case 'PLUMBER': return '🚰';
      case 'AC_REPAIR': return '❄️';
      case 'TV_REPAIR': return '📺';
      case 'LAPTOP_REPAIR': return '💻';
      case 'CLEANING': return '🧹';
      case 'PEST_CONTROL': return '🐜';
      case 'PET_CARE': return '🐶';
      default: return '🛠️';
    }
  };

  const fetchCategories = async () => {
    setLoading(true);
    try {
      const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080';
      const res = await fetch(`${apiUrl}/api/v1/service-categories?size=100`);
      if (res.ok) {
        const data = await res.json();
        if (data.data && data.data.content) {
          setCategories(data.data.content);
        }
      }
    } catch (err) {
      console.error("Failed to load service categories", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleOpen = (category = null) => {
    if (category) {
      setEditing(category);
      setFormData({
        name: category.name || '',
        code: category.code || '',
        description: category.description || '',
        displayOrder: category.displayOrder || 0
      });
    } else {
      setEditing(null);
      setFormData({ name: '', code: '', description: '', displayOrder: 0 });
    }
    setOpen(true);
  };

  const handleClose = () => setOpen(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.name || !formData.code) return;

    try {
      const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080';
      const method = editing ? 'PUT' : 'POST';
      const url = editing 
        ? `${apiUrl}/api/v1/service-categories/${editing.id}`
        : `${apiUrl}/api/v1/service-categories`;

      const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          name: formData.name,
          code: formData.code.toUpperCase(),
          description: formData.description,
          displayOrder: parseInt(formData.displayOrder)
        })
      });

      if (res.ok) {
        alert(editing ? "Category updated successfully!" : "Category created successfully!");
        handleClose();
        fetchCategories();
      } else {
        const err = await res.json();
        alert(err.message || "Failed to save category.");
      }
    } catch (err) {
      console.error(err);
      alert("Error contacting the server.");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this category?")) return;
    try {
      const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080';
      const res = await fetch(`${apiUrl}/api/v1/service-categories/${id}`, {
        method: 'DELETE'
      });
      if (res.ok) {
        alert("Category deleted successfully!");
        fetchCategories();
      } else {
        alert("Failed to delete category.");
      }
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <Container maxWidth="xl">
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4">Service Categories Configurator</Typography>
        <Button variant="contained" color="primary" onClick={() => handleOpen()}>
          Add Category
        </Button>
      </Box>

      <TableContainer component={Paper} className="glass-panel">
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Icon</TableCell>
              <TableCell>Code</TableCell>
              <TableCell>Category Name</TableCell>
              <TableCell>Description</TableCell>
              <TableCell>Display Order</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {categories.map(c => (
              <TableRow key={c.id}>
                <TableCell style={{ fontSize: 24 }}>{getCategoryIcon(c.code)}</TableCell>
                <TableCell><Chip label={c.code} size="small" variant="outlined" color="primary" /></TableCell>
                <TableCell style={{ fontWeight: 'bold' }}>{c.name}</TableCell>
                <TableCell>{c.description || 'No description provided'}</TableCell>
                <TableCell>{c.displayOrder}</TableCell>
                <TableCell align="right">
                  <Button size="small" color="info" onClick={() => handleOpen(c)} sx={{ mr: 1 }}>Edit</Button>
                  <Button size="small" color="error" onClick={() => handleDelete(c.id)}>Delete</Button>
                </TableCell>
              </TableRow>
            ))}
            {categories.length === 0 && (
              <TableRow>
                <TableCell colSpan={6} align="center">
                  {loading ? "Loading categories..." : "No categories defined. Click Add Category to create one!"}
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Add / Edit Dialog */}
      <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
        <DialogTitle>{editing ? "Edit Service Category" : "Add Service Category"}</DialogTitle>
        <Box component="form" onSubmit={handleSubmit}>
          <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              label="Category Name"
              required
              fullWidth
              value={formData.name}
              onChange={e => setFormData({ ...formData, name: e.target.value })}
            />
            <TextField
              label="Category Code (e.g. ELECTRICIAN)"
              required
              fullWidth
              disabled={!!editing}
              value={formData.code}
              onChange={e => setFormData({ ...formData, code: e.target.value })}
            />
            <TextField
              label="Description"
              fullWidth
              multiline
              rows={3}
              value={formData.description}
              onChange={e => setFormData({ ...formData, description: e.target.value })}
            />
            <TextField
              label="Display Order (priority)"
              type="number"
              fullWidth
              value={formData.displayOrder}
              onChange={e => setFormData({ ...formData, displayOrder: e.target.value })}
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={handleClose}>Cancel</Button>
            <Button type="submit" variant="contained" color="primary">Save</Button>
          </DialogActions>
        </Box>
      </Dialog>
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
