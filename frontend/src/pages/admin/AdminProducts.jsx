import { useEffect, useState } from 'react';
import { Badge, Button, Form, Modal, Row, Col, Spinner, Table } from 'react-bootstrap';
import { categoryApi, productApi } from '../../api/services.js';
import { resolveImage } from '../../api/client.js';
import { useToast } from '../../context/ToastContext.jsx';
import { extractApiError, formatPrice } from '../../utils/format.js';

const EMPTY = {
  name: '', description: '', price: '', dimensions: '',
  imageUrl: '', stock: 0, available: true, categoryId: '',
};

const AdminProducts = () => {
  const toast = useToast();
  const [items, setItems] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [show, setShow] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(EMPTY);
  const [saving, setSaving] = useState(false);

  const load = () => {
    setLoading(true);
    Promise.all([productApi.list(), categoryApi.list()])
      .then(([p, c]) => {
        setItems(p.data);
        setCategories(c.data);
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => { load(); }, []);

  const openAdd = () => {
    setEditing(null);
    setForm({ ...EMPTY, categoryId: categories[0]?.id || '' });
    setShow(true);
  };

  const openEdit = (p) => {
    setEditing(p);
    setForm({
      name: p.name, description: p.description || '', price: p.price,
      dimensions: p.dimensions || '', imageUrl: p.imageUrl || '', stock: p.stock,
      available: p.available, categoryId: p.categoryId,
    });
    setShow(true);
  };

  const save = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const payload = {
        ...form,
        price: Number(form.price),
        stock: Number(form.stock),
        categoryId: Number(form.categoryId),
      };
      if (editing) {
        await productApi.update(editing.id, payload);
        toast.success('Proizvod je ažuriran');
      } else {
        await productApi.create(payload);
        toast.success('Proizvod je dodat');
      }
      setShow(false);
      load();
    } catch (err) {
      toast.error(extractApiError(err));
    } finally {
      setSaving(false);
    }
  };

  const del = async (p) => {
    if (!window.confirm(`Obrisati proizvod "${p.name}"?`)) return;
    try {
      await productApi.delete(p.id);
      toast.success('Proizvod je obrisan');
      load();
    } catch (err) {
      toast.error(extractApiError(err));
    }
  };

  return (
    <>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="m-0">Proizvodi</h3>
        <Button className="btn-brand" onClick={openAdd} disabled={categories.length === 0}>
          <i className="bi bi-plus-circle me-1"></i> Novi proizvod
        </Button>
      </div>

      {loading ? <Spinner /> : (
        <Table responsive hover className="bg-white shadow-sm align-middle">
          <thead className="table-light">
            <tr>
              <th></th>
              <th>Naziv</th>
              <th>Kategorija</th>
              <th>Cena</th>
              <th>Stanje</th>
              <th>Status</th>
              <th className="text-end">Akcije</th>
            </tr>
          </thead>
          <tbody>
            {items.map((p) => (
              <tr key={p.id}>
                <td>
                  {p.imageUrl && (
                    <img src={resolveImage(p.imageUrl)} alt="" style={{ width: 50, height: 50, objectFit: 'cover', borderRadius: 4 }} />
                  )}
                </td>
                <td>
                  <div className="fw-semibold">{p.name}</div>
                  {p.dimensions && <small className="text-muted">{p.dimensions}</small>}
                </td>
                <td><Badge bg="secondary">{p.categoryName}</Badge></td>
                <td className="text-brand fw-bold">{formatPrice(p.price)}</td>
                <td>{p.stock}</td>
                <td>
                  {p.available
                    ? <Badge bg="success">Aktivan</Badge>
                    : <Badge bg="danger">Neaktivan</Badge>}
                </td>
                <td className="text-end">
                  <Button size="sm" variant="outline-primary" className="me-2" onClick={() => openEdit(p)}>
                    <i className="bi bi-pencil"></i>
                  </Button>
                  <Button size="sm" variant="outline-danger" onClick={() => del(p)}>
                    <i className="bi bi-trash"></i>
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}

      <Modal show={show} onHide={() => setShow(false)} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>{editing ? 'Izmena' : 'Novi'} proizvod</Modal.Title>
        </Modal.Header>
        <Form onSubmit={save}>
          <Modal.Body>
            <Row>
              <Col md={8}>
                <Form.Group className="mb-3">
                  <Form.Label>Naziv *</Form.Label>
                  <Form.Control value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required maxLength={150} />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Kategorija *</Form.Label>
                  <Form.Select value={form.categoryId} onChange={(e) => setForm({ ...form, categoryId: e.target.value })} required>
                    <option value="">-- Izaberi --</option>
                    {categories.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
                  </Form.Select>
                </Form.Group>
              </Col>
            </Row>
            <Form.Group className="mb-3">
              <Form.Label>Opis</Form.Label>
              <Form.Control as="textarea" rows={3} value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} maxLength={2000} />
            </Form.Group>
            <Row>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Cena (RSD) *</Form.Label>
                  <Form.Control type="number" step="0.01" min="0.01" value={form.price}
                                onChange={(e) => setForm({ ...form, price: e.target.value })} required />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Stanje *</Form.Label>
                  <Form.Control type="number" min="0" value={form.stock}
                                onChange={(e) => setForm({ ...form, stock: e.target.value })} required />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group className="mb-3">
                  <Form.Label>Dimenzije</Form.Label>
                  <Form.Control value={form.dimensions} onChange={(e) => setForm({ ...form, dimensions: e.target.value })} placeholder="20x12x5cm" />
                </Form.Group>
              </Col>
            </Row>
            <Form.Group className="mb-3">
              <Form.Label>URL slike</Form.Label>
              <Form.Control value={form.imageUrl} onChange={(e) => setForm({ ...form, imageUrl: e.target.value })} placeholder="/uploads/IMG_1062.JPG" />
            </Form.Group>
            <Form.Check type="switch" label="Proizvod je dostupan"
                        checked={form.available}
                        onChange={(e) => setForm({ ...form, available: e.target.checked })} />
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

export default AdminProducts;
