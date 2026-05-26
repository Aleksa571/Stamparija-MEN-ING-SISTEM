import { useEffect, useState } from 'react';
import { Badge, Button, Form, Spinner, Table } from 'react-bootstrap';
import { userApi } from '../../api/services.js';
import { useToast } from '../../context/ToastContext.jsx';
import { extractApiError, formatDate } from '../../utils/format.js';

const AdminUsers = () => {
  const toast = useToast();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = () => {
    setLoading(true);
    userApi.list().then((r) => setUsers(r.data)).finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, []);

  const setEnabled = async (u, val) => {
    try {
      await userApi.setEnabled(u.id, val);
      toast.success(`Korisnik ${u.username} je ${val ? 'aktiviran' : 'deaktiviran'}`);
      load();
    } catch (err) {
      toast.error(extractApiError(err));
    }
  };

  const del = async (u) => {
    if (!window.confirm(`Obrisati korisnika "${u.username}"?`)) return;
    try {
      await userApi.delete(u.id);
      toast.success('Korisnik obrisan');
      load();
    } catch (err) {
      toast.error(extractApiError(err));
    }
  };

  return (
    <>
      <h3 className="mb-4">Korisnici</h3>
      {loading ? <Spinner /> : (
        <Table responsive hover className="bg-white shadow-sm align-middle">
          <thead className="table-light">
            <tr>
              <th>Username</th>
              <th>Ime i prezime</th>
              <th>Email</th>
              <th>Telefon</th>
              <th>Registracija</th>
              <th>Role</th>
              <th>Aktivan</th>
              <th className="text-end">Akcije</th>
            </tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id}>
                <td className="fw-semibold">{u.username}</td>
                <td>{u.firstName} {u.lastName}</td>
                <td className="small">{u.email}</td>
                <td className="small">{u.phone || '-'}</td>
                <td className="small">{formatDate(u.createdAt)}</td>
                <td>
                  {[...u.roles].map((r) => (
                    <Badge key={r} bg={r === 'ROLE_ADMIN' ? 'danger' : 'secondary'} className="me-1">
                      {r.replace('ROLE_', '')}
                    </Badge>
                  ))}
                </td>
                <td>
                  <Form.Check type="switch" checked={u.enabled}
                              onChange={(e) => setEnabled(u, e.target.checked)} />
                </td>
                <td className="text-end">
                  <Button size="sm" variant="outline-danger" onClick={() => del(u)}
                          disabled={u.roles?.includes?.('ROLE_ADMIN') || u.username === 'admin'}>
                    <i className="bi bi-trash"></i>
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </>
  );
};

export default AdminUsers;
