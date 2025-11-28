import { useState } from "react";
import { useQuery, useMutation } from "convex/react";
import { api } from "../../convex/_generated/api";

interface ProfilePageProps {
  profile: any;
  user: any;
}

export default function ProfilePage({ profile, user }: ProfilePageProps) {
  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState(user?.name || "");
  const [classValue, setClassValue] = useState(profile?.class || "");
  const [section, setSection] = useState(profile?.section || "");
  const [bio, setBio] = useState(profile?.bio || "");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const updateProfile = useMutation(api.profiles.update);
  const updateUserName = useMutation(api.profiles.updateUserName);

  const handleSave = async () => {
    setIsSubmitting(true);
    try {
      // Update user name if changed
      if (name !== user?.name) {
        await updateUserName({ name });
      }
      
      // Update profile fields
      await updateProfile({
        class: classValue || undefined,
        section: section || undefined,
        bio: bio || undefined,
      });
      
      setIsEditing(false);
    } catch (error) {
      console.error("Failed to update profile:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    setName(user?.name || "");
    setClassValue(profile?.class || "");
    setSection(profile?.section || "");
    setBio(profile?.bio || "");
    setIsEditing(false);
  };

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      {/* Header */}
      <div className="bg-white rounded-lg shadow p-6">
        <div className="flex items-center space-x-4">
          <div className="text-6xl">👤</div>
          <div>
            <h1 className="text-2xl font-bold text-purple-600">
              My Profile {profile?.role === "teacher" ? "⭐" : ""}
            </h1>
            <p className="text-gray-600">
              {profile?.role === "teacher" ? "Teacher Account" : "Student Account"}
            </p>
          </div>
        </div>
      </div>

      {/* Profile Form */}
      <div className="bg-white rounded-lg shadow p-6">
        <div className="space-y-6">
          {/* Name */}
          <div>
            <label className="block text-sm font-medium text-purple-600 mb-2">
              Full Name
            </label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              disabled={!isEditing}
              className={`w-full px-3 py-2 border border-gray-300 rounded-md ${
                isEditing ? "bg-white focus:outline-none focus:ring-2 focus:ring-purple-500" : "bg-gray-50"
              }`}
            />
          </div>

          {/* Email */}
          <div>
            <label className="block text-sm font-medium text-purple-600 mb-2">
              Email
            </label>
            <input
              type="email"
              value={user?.email || ""}
              disabled
              className="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50"
            />
          </div>

          {/* Class (Students Only) */}
          {profile?.role === "student" && (
            <div>
              <label className="block text-sm font-medium text-purple-600 mb-2">
                Class
              </label>
              <input
                type="text"
                value={classValue}
                onChange={(e) => setClassValue(e.target.value)}
                disabled={!isEditing}
                placeholder="e.g., Computer Science"
                className={`w-full px-3 py-2 border border-gray-300 rounded-md ${
                  isEditing ? "bg-white focus:outline-none focus:ring-2 focus:ring-purple-500" : "bg-gray-50"
                }`}
              />
            </div>
          )}

          {/* Section (Students Only) */}
          {profile?.role === "student" && (
            <div>
              <label className="block text-sm font-medium text-purple-600 mb-2">
                Section
              </label>
              <input
                type="text"
                value={section}
                onChange={(e) => setSection(e.target.value)}
                disabled={!isEditing}
                placeholder="e.g., A"
                className={`w-full px-3 py-2 border border-gray-300 rounded-md ${
                  isEditing ? "bg-white focus:outline-none focus:ring-2 focus:ring-purple-500" : "bg-gray-50"
                }`}
              />
            </div>
          )}

          {/* Bio */}
          <div>
            <label className="block text-sm font-medium text-purple-600 mb-2">
              Bio
            </label>
            <textarea
              value={bio}
              onChange={(e) => setBio(e.target.value)}
              disabled={!isEditing}
              placeholder="Tell us about yourself..."
              rows={4}
              className={`w-full px-3 py-2 border border-gray-300 rounded-md ${
                isEditing ? "bg-white focus:outline-none focus:ring-2 focus:ring-purple-500" : "bg-gray-50"
              }`}
            />
          </div>

          {/* Buttons */}
          <div className="flex justify-center space-x-4">
            {!isEditing ? (
              <button
                onClick={() => setIsEditing(true)}
                className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
              >
                ✏️ Edit Profile
              </button>
            ) : (
              <>
                <button
                  onClick={handleCancel}
                  className="px-6 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700"
                >
                  ❌ Cancel
                </button>
                <button
                  onClick={handleSave}
                  disabled={isSubmitting}
                  className="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:opacity-50"
                >
                  {isSubmitting ? "Saving..." : "💾 Save Changes"}
                </button>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
