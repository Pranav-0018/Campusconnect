import { useState } from "react";
import { useQuery, useMutation } from "convex/react";
import { api } from "../../convex/_generated/api";

interface EventsPageProps {
  profile: any;
  user: any;
}

export default function EventsPage({ profile, user }: EventsPageProps) {
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [eventDate, setEventDate] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const events = useQuery(api.events.list) || [];
  const createEvent = useMutation(api.events.create);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim() || !description.trim() || !eventDate) return;

    setIsSubmitting(true);
    try {
      await createEvent({
        title: title.trim(),
        description: description.trim(),
        eventDate,
      });
      setTitle("");
      setDescription("");
      setEventDate("");
      setShowCreateForm(false);
    } catch (error) {
      console.error("Failed to create event:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">📅 Campus Events & Hackathons</h1>
        {profile?.role === "teacher" && (
          <button
            onClick={() => setShowCreateForm(!showCreateForm)}
            className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700"
          >
            ➕ Add Event
          </button>
        )}
      </div>

      {/* Create Event Form */}
      {showCreateForm && profile?.role === "teacher" && (
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Create New Event</h2>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Event Title
              </label>
              <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Enter event title..."
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Description
              </label>
              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Describe the event..."
                rows={4}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Event Date
              </label>
              <input
                type="date"
                value={eventDate}
                onChange={(e) => setEventDate(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
              />
            </div>
            <div className="flex justify-end space-x-3">
              <button
                type="button"
                onClick={() => setShowCreateForm(false)}
                className="px-4 py-2 text-gray-600 bg-gray-100 rounded-md hover:bg-gray-200"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={isSubmitting || !title.trim() || !description.trim() || !eventDate}
                className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:opacity-50"
              >
                {isSubmitting ? "Creating..." : "💾 Create Event"}
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Events List */}
      <div className="space-y-4">
        {events.length === 0 ? (
          <div className="bg-white rounded-lg shadow p-8 text-center">
            <p className="text-gray-500">No events scheduled yet.</p>
          </div>
        ) : (
          events.map((event) => (
            <div key={event._id} className="bg-white rounded-lg shadow border-l-4 border-purple-500 p-6">
              <div className="flex justify-between items-start mb-4">
                <h3 className="text-xl font-bold text-purple-600">{event.title}</h3>
                <span className="text-red-600 font-semibold">
                  📅 {new Date(event.eventDate).toLocaleDateString()}
                </span>
              </div>
              <p className="text-gray-800 mb-4 whitespace-pre-wrap">{event.description}</p>
              <p className="text-sm text-gray-500">
                Organized by: {event.createdByName} ⭐
              </p>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
