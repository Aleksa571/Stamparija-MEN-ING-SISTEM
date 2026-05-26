import { useEffect, useState } from 'react';
import { Badge, Button, Form, Modal, Spinner, Table } from 'react-bootstrap';
import { categoryApi } from '../../api/services.js';
import { resolveImage } from '../../api/client.js';
import { useToast } from '../../context/ToastContext.jsx';
import { extractApiError } from '../../utils/format.js';

const EMPTY = { name: '', description: '', imageUrl: '' };

const AdminCategories = () => {
  const toast = useToast();
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [show, setShow] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(EMPTY);
  const [saving, setSaving] = useState(false);

  const load = () => {
    setLoading(true);
    categoryApi.list().then((r) => setItems(r.data)).finally(() => setLoading(false));
  };

  useEffect(() => { load(); }, []);

  const openAdd = () => { setEditing(null); setForm(EMPTY); setShow(true); };
  const openEdit = (c) => {
    setEditing(c);
    setForm({ name: c.name, description: c.description || '', imageUrl: c.imageUrl || '' });
    setShow(true);
  };

  const save = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (editing) {
        await categoryApi.update(editing.id, form);
        toast.success('Kategorija je ažurirana');
      } else {
        await categoryApi.create(form);
        toast.success('Kategorija je dodata');
      }
      setShow(false);
      load();
    } catch (err) {
      toast.error(extractApiError(err));
    } finally {
      setSaving(false);
    }
  };

  const del = async (c) => {
    if (!window.confirm(`Da li ste sigurni da želite da obrišete kategoriju "${c.name}"?`)) return;
    try {
      await categoryApi.delete(c.id);
      toast.success('Kategorija je obrisana');
      load();
    } catch (err) {
      toast.error(extractApiError(err));
    }
  };

  return (
    <>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="m-0">Kategorije</h3>
        <Button className="btn-brand" onClick={openAdd}>
          <i className="bi bi-plus-circle me-1"></i> Nova kategorija
        </Button>
      </div>
      {loading ? <Spinner /> : (
        <Table responsive hover className="bg-white shadow-sm align-middle">
          <thead className="table-light">
            <tr>
              <th>Slika</th>
              <th>Naziv</th>
              <th>Opis</th>
              <th>Proizvoda</th>
              <th className="text-end">Akcije</th>
            </tr>
          </thead>
          <tbody>
            {items.map((c) => (
              <tr key={c.id}>
                <td>
                  {c.imageUrl && (
                    <img src={resolveImage(c.imageUrl)} alt="" style={{ width: 60, height: 40, objectFit: 'cover', borderRadius: 4 }} />
                  )}
                </td>
                <td className="fw-semibold">{c.name}</td>
                <td className="text-muted small">{c.description?.substring(0, 80)}{c.description?.length > 80 ? '...' : ''}</td>
                <td><Badge bg="secondary">{c.productCount}</Badge></td>
                <td className="text-end">
                  <Button size="sm" variant="outline-primary" className="me-2" onClick={() => openEdit(c)}>
                    <i className="bi bi-pencil"></i>
                  </Button>
                  <Button size="sm" variant="outline-danger" onClick={() => del(c)}>
                    <i className="bi bi-trash"></i>
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}

      <Modal show={show} onHide={() => setShow(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{editing ? 'Izmena' : 'Nova'} kategorija</Modal.Title>
        </Modal.Header>
        <Form onSubmit={save}>
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>Naziv *</Form.Label>
              <Form.Control
                value={form.name}
                onChange={(e) => setForm({ ...form, name: e.target.value })}
                required maxLength={100}
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Opis</Form.Label>
              <Form.Control
                as="textarea" rows={3}
                value={form.description}
                onChange={(e) => setForm({ ...form, description: e.target.value })}
                maxLength={500}
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>URL slike</Form.Label>
              <Form.Control
                value={form.imageUrl}
                onChange={(e) => setForm({ ...form, imageUrl: e.target.value })}
                placeholder="/uploads/IMG_1062.JPG ili https://..."
              />
              <Form.Text className="text-muted">Postojeće slike: /uploads/IMG_1062.JPG do IMG_1070.JPG</Form.Text>
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShow(false)}>Otkaži</Button>
            <Button type="submit" className="btn-brand" disabled={saving}>
              {saving ? <Spinner size="sm" /> : 'Sačuvaj'}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </>
  );
};

export default AdminCategories;
